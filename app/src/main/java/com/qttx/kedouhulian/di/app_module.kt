package com.qttx.kedouhulian.di

import com.qttx.kedouhulian.BuildConfig
import com.qttx.kedouhulian.net.Api
import com.qttx.kedouhulian.ui.chat.viewModel.*
import com.qttx.kedouhulian.ui.common.viewModel.PayViewModel
import com.qttx.kedouhulian.ui.pond.viewModel.MyPondViewModel
import com.qttx.kedouhulian.ui.pond.viewModel.PondDetailViewModel
import com.qttx.kedouhulian.ui.pond.viewModel.PondHomeViewModel
import com.qttx.kedouhulian.ui.reward.buunty.viewModel.*
import com.qttx.kedouhulian.ui.reward.msg.viewModel.HomeMsgViewModel
import com.qttx.kedouhulian.ui.reward.redpacket.viewModel.*
import com.qttx.kedouhulian.ui.reward.task.viewModel.*
import com.qttx.kedouhulian.ui.reward.viewModel.SettingNotifyViewModel
import com.qttx.kedouhulian.ui.reward.viewModel.SoceViewModel
import com.qttx.kedouhulian.ui.trade.viewModel.*
import com.qttx.kedouhulian.ui.user.viewModel.*
import com.qttx.kedouhulian.ui.wallet.viewModel.WalletKeDouListViewModel
import com.qttx.kedouhulian.ui.wallet.viewModel.WalletListViewModel
import com.qttx.kedouhulian.ui.wallet.viewModel.WalletViewModel
import com.stay.toolslibrary.net.NetManager
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit

/**
 * @author huangyr
 * @date 2019/2/18 0018
 */
val viewModel_User = module {
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { FindPsddViewModel(get()) }
    viewModel { SMSViewModel(get()) }
    viewModel { PerfectUserDataViewModel(get()) }
    viewModel { UserInfoViewModel(get()) }
    viewModel { WalletViewModel(get()) }
    viewModel { WalletListViewModel(get()) }
    viewModel { WalletKeDouListViewModel(get()) }

}
val viewModel_Task = module {
    viewModel { PayViewModel(get()) }
    viewModel { TaskViewModel(get()) }
    viewModel { TaskPublishViewModel(get()) }
    viewModel { TaskDetailViewModel(get()) }
    viewModel { TaskSubmitViewModel(get()) }
    viewModel { WaitVerifyTaskViewModel(get()) }
    viewModel { MyPublishTaskViewModel(get()) }
    viewModel { TaskCommentViewModel(get()) }
    viewModel { MyGrapTaskViewModel(get()) }
    viewModel { TaskComplainViewModel(get()) }
    viewModel { RedPacketViewModel(get()) }
    viewModel { RedPacketDetailViewModel(get()) }
    viewModel { RedPacketPublishViewModel(get()) }
    viewModel { MyPublishRedPacketViewModel(get()) }
    viewModel { MyGrapRedPacketViewModel(get()) }
    viewModel { BountyPublishViewModel(get()) }
    viewModel { BountyViewModel(get()) }
    viewModel { BountyDetailViewModel(get()) }
    viewModel { MyPublishBountyViewModel(get()) }
    viewModel { MyGrapBountyViewModel(get()) }
    viewModel { SettingNotifyViewModel(get()) }
    viewModel { SoceViewModel(get()) }
    viewModel { HomeMsgViewModel(get()) }
    viewModel { UserCopartnerViewModel(get()) }

}
val viewModel_Pond = module {
    viewModel { PondHomeViewModel(get()) }
    viewModel { MyPondViewModel(get()) }
    viewModel { PondDetailViewModel(get()) }
}
val viewModel_Trade = module {
    viewModel { TradeMarketViewModel(get()) }
    viewModel { TradeDetailViewModel(get()) }
    viewModel { TradePublishViewModel(get()) }
    viewModel { MyPublishTradeEndViewModel(get()) }
    viewModel { MyPublishTradeIngViewModel(get()) }
    viewModel { MyBuyTradeViewModel(get()) }
}

val viewModel_Chat = module {
    viewModel { ChatViewModel(get()) }
    viewModel { ChatGroupViewModel(get()) }
    viewModel { ChatBountyViewModel(get()) }
    viewModel {(id:String)-> ChatApplyViewModel(get(),id) }
    viewModel { ChatCustomMsgViewModel(get()) }

    viewModel { ZhuanZhangViewModel(get()) }


}


val http_module = module {
    single(name = "app") { NetManager.getRetrofit(BuildConfig.BaseUrl, get()) }
    single {
        get<Retrofit>(name = "app").create(Api::class.java)
    }
}

val moduleList = listOf(viewModel_User,
        viewModel_Task,
        http_module, viewModel_Pond, viewModel_Trade,viewModel_Chat)
