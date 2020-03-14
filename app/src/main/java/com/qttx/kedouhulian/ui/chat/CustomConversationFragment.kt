package com.qttx.kedouhulian.ui.chat

import android.view.MenuItem
import io.rong.imkit.fragment.ConversationFragment

/**
 * @author huangyr
 * @date 2019/6/18 0018
 */
class CustomConversationFragment : ConversationFragment()
{
    override fun onContextItemSelected(item: MenuItem?): Boolean {
        return super.onContextItemSelected(item)

    }

    override fun showMoreClickItem(): Boolean {
        return true;
    }
}