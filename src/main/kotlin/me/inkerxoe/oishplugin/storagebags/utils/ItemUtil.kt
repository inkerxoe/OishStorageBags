package me.inkerxoe.oishplugin.storagebags.utils

import org.bukkit.configuration.ConfigurationSection
import taboolib.module.nms.ItemTag
import taboolib.module.nms.ItemTagData
import taboolib.module.nms.ItemTagList
import taboolib.module.nms.ItemTagType

/**
 * OishStorageBags
 * me.inkerxoe.oishplugin.storagebags.utils
 *
 * @author InkerXoe
 * @since 2024/2/17 下午3:23
 */
/**
 * 物品相关工具类
 */
object ItemUtil {
    val invalidNBT by lazy { arrayListOf("Enchantments","VARIABLES_DATA","ench","Damage","HideFlags","Unbreakable", "CustomModelData") }

    /**
     * HashMap 转 ItemTag
     *
     * @return 转换结果
     */
    @JvmStatic
    fun HashMap<*, *>.toItemTag(): ItemTag {
        val itemTag = ItemTag()
        for ((key, value) in this) {
            itemTag[(key as String)] = value.toItemTagData()
        }
        return itemTag
    }

    @JvmStatic
    fun ConfigurationSection.toItemTag(): ItemTag {
        val itemTag = ItemTag()
        this.getKeys(false).forEach { key ->
            this.get(key)?.let { value ->
                itemTag[key] = value.toItemTagData()
            }
        }
        return itemTag
    }

    /**
     * 类型强制转换
     * 用于解析 NeigeItems 物品 NBT 配置中的类型强制转换
     *
     * @return 转换结果
     */
    @JvmStatic
    fun String.cast(): Any {
        return when {
            this.startsWith("(Byte) ") -> this.substring(7, this.length).toByteOrNull() ?: this
            this.startsWith("(Short) ") -> this.substring(8, this.length).toShortOrNull() ?: this
            this.startsWith("(Int) ") -> this.substring(6, this.length).toIntOrNull() ?: this
            this.startsWith("(Long) ") -> this.substring(7, this.length).toLongOrNull() ?: this
            this.startsWith("(Float) ") -> this.substring(8, this.length).toFloatOrNull() ?: this
            this.startsWith("(Double) ") -> this.substring(9, this.length).toDoubleOrNull() ?: this
            this.startsWith("[") && this.endsWith("]") -> {
                val list = this.substring(1, this.lastIndex).split(",").map { it.cast() }
                when {
                    list.all { it is Byte } -> ByteArray(list.size){ list[it] as Byte }
                    list.all { it is Int } -> IntArray(list.size){ list[it] as Int }
                    else -> this
                }
            }
            else -> this
        }
    }

    /**
     * 类型强制转换
     * 用于解析 NeigeItems 物品 NBT 配置中的类型强制转换
     *
     * @return 转换结果
     */
    @JvmStatic
    fun Any.cast(): Any {
        return when (this) {
            is String -> this.cast()
            else -> this
        }
    }

    /**
     * 类型强制转换
     * 用于解析 NeigeItems 物品 NBT 配置中的类型强制转换
     *
     * @return 转换结果
     */
    @JvmStatic
    fun String.castToItemTagData(): ItemTagData {
        return when {
            this.startsWith("(Byte) ") -> {
                this.substring(7, this.length).toByteOrNull()?.let { ItemTagData(it) } ?: ItemTagData(this)
            }
            this.startsWith("(Short) ") -> {
                this.substring(8, this.length).toShortOrNull()?.let { ItemTagData(it) } ?: ItemTagData(this)
            }
            this.startsWith("(Int) ") -> {
                this.substring(6, this.length).toIntOrNull()?.let { ItemTagData(it) } ?: ItemTagData(this)
            }
            this.startsWith("(Long) ") -> {
                this.substring(7, this.length).toLongOrNull()?.let { ItemTagData(it) } ?: ItemTagData(this)
            }
            this.startsWith("(Float) ") -> {
                this.substring(8, this.length).toFloatOrNull()?.let { ItemTagData(it) } ?: ItemTagData(this)
            }
            this.startsWith("(Double) ") -> {
                this.substring(9, this.length).toDoubleOrNull()?.let { ItemTagData(it) } ?: ItemTagData(this)
            }
            this.startsWith("[") && this.endsWith("]") -> {
                val list = this.substring(1, this.lastIndex).split(",").map { it.cast() }
                when {
                    list.all { it is Byte } -> ByteArray(list.size){ list[it] as Byte }.toItemTagData()
                    list.all { it is Int } -> IntArray(list.size){ list[it] as Int }.toItemTagData()
                    else -> ItemTagData(this)
                }
            }
            else -> ItemTagData(this)
        }
    }

    /**
     * 转 ItemTagData
     *
     * @return 转换结果
     */
    @JvmStatic
    fun Any.toItemTagData(): ItemTagData {
        return when (this) {
            is ItemTagData -> this
            is Byte -> ItemTagData(this)
            is Short -> ItemTagData(this)
            is Int -> ItemTagData(this)
            is Long -> ItemTagData(this)
            is Float -> ItemTagData(this)
            is Double -> ItemTagData(this)
            is ByteArray -> ItemTagData(this)
            is IntArray -> ItemTagData(this)
            is String -> this.castToItemTagData()
            is List<*> -> {
                val list = this.map { it?.cast() ?: it }
                when {
                    list.all { it is Byte } -> ByteArray(list.size){ list[it] as Byte }.toItemTagData()
                    list.all { it is Int } -> IntArray(list.size){ list[it] as Int }.toItemTagData()
                    else -> {
                        val itemTagList = ItemTagList()
                        for (obj in list) {
                            obj?.toItemTagData()?.let { itemTagList.add(it) }
                        }
                        itemTagList
                    }
                }
            }
            is HashMap<*, *> -> ItemTagData(this.toItemTag())
            is ConfigurationSection -> ItemTagData(this.toItemTag())
            else -> ItemTagData("nm的你塞了个什么东西进来，我给你一拖鞋")
        }
    }

    /**
     * ItemTag 转 HashMap
     *
     * @param invalidNBT 转换过程中忽视对应NBT
     * @return 转换结果
     */
    @JvmStatic
    fun ItemTag.toMap(invalidNBT: List<String>): HashMap<String, Any> {
        val hashMap = HashMap<String, Any>()
        for ((key, value) in this) {
            if (!invalidNBT.contains(key)) {
                hashMap[key] = value.parseValue()
            }
        }
        return hashMap
    }

    /**
     * ItemTag 转 HashMap
     *
     * @return 转换结果
     */
    @JvmStatic
    fun ItemTag.toMap(): HashMap<String, Any> {
        val hashMap = HashMap<String, Any>()
        for ((key, value) in this) {
            hashMap[key] = value.parseValue()
        }
        return hashMap
    }

    /**
     * ItemTagData 解析
     *
     * @return 解析结果
     */
    @JvmStatic
    fun ItemTagData.parseValue(): Any {
        return when (this.type) {
            ItemTagType.BYTE -> "(Byte) ${this.asString()}"
            ItemTagType.SHORT ->  "(Short) ${this.asString()}"
            ItemTagType.INT ->  "(Int) ${this.asString()}"
            ItemTagType.LONG ->  "(Long) ${this.asString()}"
            ItemTagType.FLOAT ->  "(Float) ${this.asString()}"
            ItemTagType.DOUBLE ->  "(Double) ${this.asString()}"
            ItemTagType.STRING ->  this.asString()
            ItemTagType.BYTE_ARRAY -> this.asByteArray().toList().map { "(Byte) $it" }
            ItemTagType.INT_ARRAY -> this.asIntArray().toList().map { "(Int) $it" }
            ItemTagType.COMPOUND -> this.asCompound().toMap()
            ItemTagType.LIST -> this.asList().map { it.parseValue() }
            else -> this.asString()
        }
    }

    /**
     * ItemTag 合并(后者覆盖前者)
     *
     * @param itemTag 用于合并覆盖
     * @return 合并结果
     */
    @JvmStatic
    fun ItemTag.coverWith(itemTag: ItemTag): ItemTag {
        // 遍历附加NBT
        for ((key, value) in itemTag) {
            // 如果二者包含相同键
            val overrideValue = this[key]
            if (overrideValue != null) {
                // 如果二者均为COMPOUND
                if (overrideValue.type == ItemTagType.COMPOUND
                    && value.type == ItemTagType.COMPOUND) {
                    // 合并
                    this[key] = overrideValue.asCompound().coverWith(value.asCompound())
                } else {
                    // 覆盖
                    this[key] = value
                }
                // 这个键原NBT里没有
            } else {
                // 添加
                this[key] = value
            }
        }
        return this
    }

    /**
     * 获取ItemTag中的值(key以.作分隔, 以\转义), 获取不到返回null
     *
     * @param key ItemTag键
     * @return ItemTag值(获取不到返回null)
     */
    @JvmStatic
    fun ItemTag.getDeepOrNull(key: String): ItemTagData? {
        // 当前层级ItemTagData
        var value: ItemTagData? = this
        // key以.作分隔
        val args = key.split('.', '\\')

        // 逐层深入
        args.forEach { currentKey ->
            // 检测当前ItemTagData类型
            when (value?.type) {
                ItemTagType.LIST -> {
                    // key无法转换为数字/数组越界将返回null, 其他情况下将返回下一级
                    value = currentKey.toIntOrNull()?.coerceAtLeast(0)?.let { index ->
                        val list = value!!.asList()
                        when {
                            list.size > index -> list[index]
                            else -> return null
                        }
                    }
                }
                ItemTagType.BYTE_ARRAY -> {
                    // key无法转换为数字/数组越界将返回null, 其他情况下将返回下一级
                    value = currentKey.toIntOrNull()?.coerceAtLeast(0)?.let { index ->
                        val array = value!!.asByteArray()
                        when {
                            array.size > index -> ItemTagData(array[index])
                            else -> return null
                        }
                    }
                }
                ItemTagType.INT_ARRAY -> {
                    // key无法转换为数字/数组越界将返回null, 其他情况下将返回下一级
                    value = currentKey.toIntOrNull()?.coerceAtLeast(0)?.let { index ->
                        val array = value!!.asIntArray()
                        when {
                            array.size > index -> ItemTagData(array[index])
                            else -> return null
                        }
                    }
                }
                ItemTagType.COMPOUND -> {
                    // 当前Compound不含对应的key将返回null, 其他情况下将返回下一级
                    value = value!!.asCompound()[currentKey]
                }
                // 可能情况: 数组索引不是数字/数组越界/当前Compound不含对应的key/ItemTag层数不够
                else -> return null
            }
        }

        return value
    }

    /**
     * 向ItemTag中插入一个值(key以.作分隔, 以\转义). 我这代码写得依托答辩, 建议不要用.
     *
     * @param key ItemTag键
     * @param value ItemTag值
     */
    @JvmStatic
    fun ItemTag.putDeepFixed(key: String, value: ItemTagData) {
        // 父级ItemTag
        var father: ItemTagData = this
        // 当前ItemTagData的Id
        var tempId = ""
        // 当前ItemTagData
        var temp: ItemTagData = this
        // 待获取ItemTag键
        val args = key.split('.', '\\')

        // 逐层深入
        for (index in 0 until (args.size - 1)) {
            // 获取下一级Id
            val node = args[index]
            // 判断当前ItemTagData类型
            when (temp.type) {
                // 是COMPOUND
                ItemTagType.COMPOUND -> {
                    // 记录父级ItemTag
                    father = temp
                    // 获取下一级, 如果下一级是空的就创建一个新ItemTag()丢进去
                    temp = temp.asCompound().computeIfAbsent(node) {
                        ItemTag()
                    }
                    // 记录当前ItemTagData的Id
                    tempId = node
                }
                // 其他情况
                else -> {
                    // 新建一个ItemTag
                    val fatherItemTag = ItemTag()
                    // 覆盖上一层
                    father.asCompound()[tempId] = fatherItemTag
                    // 新建当前ItemTagData
                    val tempItemTag = ItemTag()
                    // 建立下一级ItemTagData
                    fatherItemTag[node] = tempItemTag
                    // 记录父级ItemTag
                    father = fatherItemTag
                    // 记录当前ItemTagData
                    temp = tempItemTag
                    // 记录当前ItemTagData的Id
                    tempId = node
                }
            }
        }

        // 已达末级
        val node = args[args.lastIndex]
        if (temp.type == ItemTagType.COMPOUND) {
            // 东西丢进去
            temp.asCompound()[node] = value
            // 如果当前ItemTagData是其他类型
        } else {
            // 新建一个ItemTag
            val newItemTag = ItemTag()
            // 东西丢进去
            newItemTag[node] = value
            // 覆盖上一层
            father.asCompound()[tempId] = newItemTag
        }
    }

    /**
     * 向ItemTag中插入一个值(key以.作分隔, 以\转义, 如果key可以转换成Int, 就认为你当前层级是一个ItemTagList, 而非ItemTag). 我这代码写得依托答辩, 建议不要用.
     *
     * @param key ItemTag键
     * @param value ItemTag值
     */
    @JvmStatic
    fun ItemTag.putDeepWithList(key: String, value: ItemTagData) {
        // 父级ItemTag
        var father: ItemTagData = this
        // 当前ItemTagData的Id
        var tempId = ""
        // 当前ItemTagData
        var temp: ItemTagData = this
        // 待获取ItemTag键
        val args = key.split('.', '\\')

        // 逐层深入
        for (index in 0 until (args.size - 1)) {
            // 获取下一级Id
            val node = args[index]
            val nodeIndex = node.toIntOrNull()
            // 我姑且认为没炸
            var boom = false
            let {
                if (nodeIndex != null) {
                    // 判断当前ItemTagData类型
                    when (temp.type) {
                        // 是LIST
                        ItemTagType.LIST -> {
                            val originFather = father
                            // 记录父级ItemTagList
                            father = temp
                            // 获取下一级
                            val content = temp.asList().getOrNull(nodeIndex)
                            // 如果下一级有东西
                            if (content != null) {
                                // 皆大欢喜
                                temp = content
                                // 如果下一级没东西
                            } else {
                                // 如果刚好是比list的大小多一个
                                if (nodeIndex == temp.asList().size) {
                                    // 创建一个新的ItemTag
                                    val newItemTag = ItemTag()
                                    // 丢进去
                                    temp.asList().add(newItemTag)
                                    // 记录一下
                                    temp = newItemTag
                                    // 如果现在这个index很离谱
                                } else {
                                    // 你妈, 爬(变成ItemTag)
                                    boom = true
                                    father = originFather
                                    return@let
                                }
                            }
                            // 记录当前ItemTagData的Id
                            tempId = node
                        }
                        // 其他情况
                        else -> {
                            // 其他情况说明需要重新创建一个ItemTagList, 所以nodeIndex必须为0
                            if (nodeIndex == 0) {
                                // 新建一个ItemTag
                                val fatherItemTagList = ItemTagList()
                                // 覆盖上一层
                                father.asCompound()[tempId] = fatherItemTagList
                                // 新建当前ItemTagData
                                val tempItemTag = ItemTag()
                                // 建立下一级ItemTagData
                                fatherItemTagList.add(tempItemTag)
                                // 记录父级ItemTag
                                father = fatherItemTagList
                                // 记录当前ItemTagData
                                temp = tempItemTag
                                // 记录当前ItemTagData的Id
                                tempId = node
                            } else {
                                // 你给我爬(变成ItemTag)
                                boom = true
                                return@let
                            }
                        }
                    }
                }
            }
            // 如果当前的键不是数字, 或者是数字但是不对劲炸了
            if (nodeIndex == null || boom) {
                // 判断当前ItemTagData类型
                when (temp.type) {
                    // 是COMPOUND
                    ItemTagType.COMPOUND -> {
                        // 记录父级ItemTag
                        father = temp
                        // 获取下一级, 如果下一级是空的就创建一个新ItemTag()丢进去
                        temp = temp.asCompound().computeIfAbsent(node) {
                            ItemTag()
                        }
                        // 记录当前ItemTagData的Id
                        tempId = node
                    }
                    // 其他情况
                    else -> {
                        // 新建一个ItemTag
                        val fatherItemTag = ItemTag()
                        // 覆盖上一层
                        father.asCompound()[tempId] = fatherItemTag
                        // 新建当前ItemTagData
                        val tempItemTag = ItemTag()
                        // 建立下一级ItemTagData
                        fatherItemTag[node] = tempItemTag
                        // 记录父级ItemTag
                        father = fatherItemTag
                        // 记录当前ItemTagData
                        temp = tempItemTag
                        // 记录当前ItemTagData的Id
                        tempId = node
                    }
                }
            }
        }

        // 已达末级
        val node = args[args.lastIndex]
        val nodeIndex = node.toIntOrNull()
        var boom = false
        // 我姑且认为没炸
        let {
            if (nodeIndex != null) {
                // ByteArray插入
                if (temp.type == ItemTagType.BYTE_ARRAY && value.type == ItemTagType.BYTE) {
                    val byteArray = temp.asByteArray()
                    // 检测是否越界
                    if (nodeIndex >= 0 && nodeIndex < byteArray.size) {
                        byteArray[nodeIndex] = value.asByte()
                        // 刚好大一个
                    } else if (nodeIndex == byteArray.size) {
                        // 复制扩容
                        val newArray = byteArray.copyOf(byteArray.size+1)
                        newArray[nodeIndex] = value.asByte()
                        // 覆盖上一层
                        when (father.type) {
                            ItemTagType.LIST -> {
                                father.asList()[tempId.toInt()] = ItemTagData(newArray)
                            }
                            else -> {
                                father.asCompound()[tempId] = ItemTagData(newArray)
                            }
                        }
                        // 越界了, 爬
                    } else {
                        // 你给我爬(变成ItemTag)
                        boom = true
                        return@let
                    }
                    // IntArray插入
                } else if (temp.type == ItemTagType.INT_ARRAY && value.type == ItemTagType.INT) {
                    val intArray = temp.asIntArray()
                    // 检测是否越界
                    if (nodeIndex >= 0 && nodeIndex < intArray.size) {
                        intArray[nodeIndex] = value.asInt()
                        // 刚好大一个
                    } else if (nodeIndex == intArray.size) {
                        // 复制扩容
                        val newArray = intArray.copyOf(intArray.size+1)
                        newArray[nodeIndex] = value.asInt()
                        // 覆盖上一层
                        when (father.type) {
                            ItemTagType.LIST -> {
                                father.asList()[tempId.toInt()] = ItemTagData(newArray)
                            }
                            else -> {
                                father.asCompound()[tempId] = ItemTagData(newArray)
                            }
                        }
                        // 越界了, 爬
                    }  else {
                        // 你给我爬(变成ItemTag)
                        boom = true
                        return@let
                    }
                    // List插入
                } else if (temp.type == ItemTagType.LIST) {
                    val list = temp.asList()
                    // 检测是否越界
                    if (nodeIndex >= 0 && nodeIndex < list.size) {
                        list[nodeIndex] = value
                        // 刚好大一个, 直接add
                    } else if (nodeIndex == list.size) {
                        list.add(value)
                        // 越界了, 爬
                    } else {
                        // 你给我爬(变成ItemTag)
                        boom = true
                        return@let
                    }
                }
            }
        }
        // 如果当前的键不是数字, 或者是数字但是不对劲炸了
        if (nodeIndex == null || boom) {
            if (temp.type == ItemTagType.COMPOUND) {
                // 东西丢进去
                temp.asCompound()[node] = value
                // 如果当前ItemTagData是其他类型
            } else {
                // 新建一个ItemTag
                val newItemTag = ItemTag()
                // 东西丢进去
                newItemTag[node] = value
                // 覆盖上一层
                father.asCompound()[tempId] = newItemTag
            }
        }
    }
}