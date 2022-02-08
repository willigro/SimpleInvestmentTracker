object Modules {

    const val app = ":app"
    const val crypto = ":crypto"
    const val datasource = ":datasource"
    const val common = ":common"
    const val commonTest = ":common-test"
    const val di = ":di"
    const val diUtils = ":di:di-utils"
    const val deposit = ":deposit"

    val modules = listOf(app, crypto, datasource, common, di, commonTest, diUtils, deposit)
}