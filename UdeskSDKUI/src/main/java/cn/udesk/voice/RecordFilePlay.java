package cn.udesk.voice;import udesk.core.model.MessageInfo;public interface RecordFilePlay {	void click(MessageInfo message, RecordPlayCallback callback);	 void recycleRes();	 void recycleCallback();	void toggle();	 String getMediaPath();		 MessageInfo getPlayAduioMessage();}