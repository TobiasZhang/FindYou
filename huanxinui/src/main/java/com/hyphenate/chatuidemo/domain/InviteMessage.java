/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hyphenate.chatuidemo.domain;

import com.hyphenate.easeui.utils.HttpContacts;

public class InviteMessage {
	private String userAvantar;
	private String truename;
	private String groupInviterAvantar;
	private String groupInviterTruename;
	public String getUserAvantar() {
		return userAvantar;
	}

	public void setUserAvantar(String userAvantar) {
		this.userAvantar = userAvantar;
	}
	public String getTruename() {
		return truename;
	}

	public void setTruename(String truename) {
		this.truename = truename;
	}
	public String getGroupInviterAvantar() {
		return groupInviterAvantar;
	}

	public void setGroupInviterAvantar(String groupInviterAvantar) {
		this.groupInviterAvantar = groupInviterAvantar;
	}

	public String getGroupInviterTruename() {
		return groupInviterTruename;
	}

	public void setGroupInviterTruename(String groupInviterTruename) {
		this.groupInviterTruename = groupInviterTruename;
	}


	private String from;
	private long time;
	private String reason;

	private InviteMesageStatus status;
	private String groupId;
	private String groupName;
	private String groupInviter;
	

	private int id;
	
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}


	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public InviteMesageStatus getStatus() {
		return status;
	}

	public void setStatus(InviteMesageStatus status) {
		this.status = status;
	}

	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public void setGroupInviter(String inviter) {
	    groupInviter = inviter;
	}
	
	public String getGroupInviter() {
	    return groupInviter;	    
	}



	public enum InviteMesageStatus{
	    
	    //==contact
		/**being invited*/
		BEINVITEED,
		/**being refused*/
		BEREFUSED,
		/**remote user already agreed*/
		BEAGREED,
		
		//==group application
		/**remote user apply to join*/
		BEAPPLYED,
		/**you have agreed to join*/
		AGREED,
		/**you refused the join request*/
		REFUSED,
		
		//==group invitation
		/**received remote user's invitation**/
		GROUPINVITATION,
		/**remote user accept your invitation**/
		GROUPINVITATION_ACCEPTED,
        /**remote user declined your invitation**/
		GROUPINVITATION_DECLINED
	}
	
}



