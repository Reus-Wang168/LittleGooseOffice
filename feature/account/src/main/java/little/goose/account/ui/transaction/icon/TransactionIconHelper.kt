package little.goose.account.ui.transaction.icon

import little.goose.account.R
import little.goose.account.data.constants.AccountConstant.EXPENSE
import little.goose.account.data.constants.AccountConstant.INCOME
import little.goose.account.data.models.TransactionIcon

object TransactionIconHelper {

    //1.修改数量
    private const val iconCount = 23

    val expenseIconList = ArrayList<TransactionIcon>()
    val incomeIconList = ArrayList<TransactionIcon>()

    init {
        for (id in 1..iconCount) {
            when (val type = getIconType(id)) {
                EXPENSE -> expenseIconList.add(
                    TransactionIcon(
                        id, type, getIconName(id), getIconPath(id)
                    )
                )
                INCOME -> incomeIconList.add(
                    TransactionIcon(
                        id, type, getIconName(id), getIconPath(id)
                    )
                )
            }
        }
    }

    //2.添加名称
    fun getIconName(iconId: Int): String {
        return when (iconId) {
            1 -> "饮食"
            2 -> "购物"
            3 -> "交通"
            4 -> "学习"
            5 -> "房租"
            6 -> "娱乐"
            7 -> "服饰"
            8 -> "化妆"
            9 -> "礼物"
            10 -> "数码"
            11 -> "工资"
            12 -> "红包"
            13 -> "其他"
            14 -> "医疗"
            15 -> "话费"
            16 -> "网费"
            17 -> "氪金"
            18 -> "理财"
            19 -> "理财"
            20 -> "零食"
            21 -> "日常"
            22 -> "理发"
            23 -> "红包"
            else -> "消费"
        }
    }

    //3.添加类型
    private fun getIconType(iconId: Int): Int {
        return when (iconId) {
            1 -> EXPENSE
            2 -> EXPENSE
            3 -> EXPENSE
            4 -> EXPENSE
            5 -> EXPENSE
            6 -> EXPENSE
            7 -> EXPENSE
            8 -> EXPENSE
            9 -> EXPENSE
            10 -> EXPENSE
            11 -> INCOME
            12 -> INCOME
            13 -> INCOME
            14 -> EXPENSE
            15 -> EXPENSE
            16 -> EXPENSE
            17 -> EXPENSE
            18 -> INCOME
            19 -> EXPENSE
            20 -> EXPENSE
            21 -> EXPENSE
            22 -> EXPENSE
            23 -> EXPENSE
            else -> EXPENSE
        }
    }

    //4.添加图标路径
    fun getIconPath(iconId: Int): Int {
        return when (iconId) {
            1 -> R.drawable.icon_eat
            2 -> R.drawable.icon_shopping
            3 -> R.drawable.icon_car
            4 -> R.drawable.icon_book
            5 -> R.drawable.icon_house
            6 -> R.drawable.icon_game
            7 -> R.drawable.icon_clothe
            8 -> R.drawable.icon_make_up
            9 -> R.drawable.icon_gift
            10 -> R.drawable.icon_camera
            11 -> R.drawable.icon_credit_card
            12 -> R.drawable.icon_red_packet
            13 -> R.drawable.icon_money
            14 -> R.drawable.icon_medical
            15 -> R.drawable.icon_phone_charge
            16 -> R.drawable.icon_router
            17 -> R.drawable.icon_give_reword
            18 -> R.drawable.icon_stock
            19 -> R.drawable.icon_stock
            20 -> R.drawable.icon_fastfood
            21 -> R.drawable.icon_blender
            22 -> R.drawable.icon_cut
            23 -> R.drawable.icon_red_packet
            else -> R.drawable.icon_money
        }
    }
}