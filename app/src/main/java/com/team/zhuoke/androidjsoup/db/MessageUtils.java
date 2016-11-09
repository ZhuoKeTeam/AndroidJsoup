package com.team.zhuoke.androidjsoup.db;

import android.os.Message;
import android.text.TextUtils;

import com.team.zhuoke.androidjsoup.db.interfaces.IMessage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * 消息帮助类
 *
 * <p>detailed comment
 * @author ztw 2016年5月3日
 * @see
 * @since 1.0
 */
public class MessageUtils
{
    private MessageUtils()
    {
    }
    
    /**
     * @param message
     * @return
     */
    public static List<Message> parseMessages(String message)
    {
        if (TextUtils.isEmpty(message))
        {
            return null;
        }
        String[] keyIds = message.split(IMessage.MSG_KIND_SPLIT);
        if (keyIds.length == 0)
        {
            return null;
        }
        List<Message> messages = new ArrayList<Message>();
        for (String keyId : keyIds)
        {
            if (TextUtils.isEmpty(keyId))
            {
                continue;
            }
            String[] ks = keyId.split(IMessage.MSG_KIND_ID_SPLIT);
            if (ks.length == 0)
            {
                continue;
            }
            int i = Integer.parseInt(ks[0]);
            Set<String> ids = new HashSet<String>();
            if (ks.length > 1)
            {
                String[] idStrs = ks[1].split(IMessage.MSG_PARSE_ID_SPLIT);
                for (String id : idStrs)
                {
                    if (TextUtils.isEmpty(keyId))
                    {
                        continue;
                    }
                    ids.add(id);
                }
            }
            Message message2 = new Message();
            message2.what = i;
            message2.obj = ids;
            messages.add(message2);
        }
        return messages;
    }
}
