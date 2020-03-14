package com.qttx.kedouhulian.ui.chat.extensionModule;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.RongExtension;
import io.rong.imkit.emoticon.IEmoticonTab;
import io.rong.imkit.plugin.DefaultLocationPlugin;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.plugin.ImagePlugin;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;


public class ExtensionModule extends DefaultExtensionModule {

    public ExtensionModule(Context context) {
        super(context);
    }

    @Override
    public void onInit(String appKey) {
        super.onInit(appKey);
    }

    @Override
    public void onDisconnect() {
        super.onDisconnect();
    }

    @Override
    public void onConnect(String token) {
        super.onConnect(token);
    }

    @Override
    public void onAttachedToExtension(RongExtension extension) {
        super.onAttachedToExtension(extension);
    }

    @Override
    public void onDetachedFromExtension() {
        super.onDetachedFromExtension();
    }

    @Override
    public void onReceivedMessage(Message message) {
        super.onReceivedMessage(message);
    }

    @Override
    public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType) {
        List<IPluginModule> pluginModuleList = new ArrayList<>();
        IPluginModule image = new ImagePlugin();
//        IPluginModule file = new FilePlugin();
        IPluginModule locationPlugin = new DefaultLocationPlugin();

        pluginModuleList.add(image);
//        pluginModuleList.add(file);
        pluginModuleList.add(locationPlugin);
        if (conversationType.equals(Conversation.ConversationType.GROUP)) {
            MsgPlugin msg = new MsgPlugin();
            pluginModuleList.add(msg);
        }else
        {
            RedPackagePlugin msg = new RedPackagePlugin();
            pluginModuleList.add(msg);
        }
        return pluginModuleList;

    }

    @Override
    public List<IEmoticonTab> getEmoticonTabs() {
        return super.getEmoticonTabs();
    }
}
