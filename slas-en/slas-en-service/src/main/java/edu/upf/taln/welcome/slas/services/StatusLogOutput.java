package edu.upf.taln.welcome.slas.services;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonValue;

public class StatusLogOutput {
	
	public static enum Status {operational, degraded, failed}
	
	public static class Log {
		private String moment;
		private String message;
		
		public String getMoment() {
			return moment;
		}
		public void setMoment(String moment) {
			this.moment = moment;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		
	}
	
	@JsonValue
	private List<Log> logs = new ArrayList<>();
	
	public StatusLogOutput() {
		
		/*SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
	    Date date = new Date();  */
	    
	    Log log = new Log();
	    log.setMoment("12/02/2021 12:21:00");
		log.setMessage("Everything is alright.");
		
		logs.add(log);
	}

	public List<Log> getLogs() {
		return logs;
	}

	public void setLogs(List<Log> logs) {
		this.logs = logs;
	}
	
}
