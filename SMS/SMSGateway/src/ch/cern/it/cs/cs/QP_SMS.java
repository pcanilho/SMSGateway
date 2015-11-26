package ch.cern.it.cs.cs;

public class QP_SMS {

	private String destination_number;
	private String unit_name;
	private MODE mode;
	
	public static enum MODE {
		DELETE_TASKS, KEEP_TASKS, APN_RESET, FTP_RESET
	}
	
	public QP_SMS(String destination_number, String unit_name, MODE mode){
		this.mode = mode;
		this.unit_name = unit_name;
		this.destination_number = destination_number;
	}
	
	public Object[] tableEntryFormat(){
		return new Object[]{destination_number, unit_name, mode};
	}
	
	public String toString() {
		return "SMS - D: " + destination_number + " ,UN: " + unit_name + " ,M: " + mode;
	}

	public String getDestination_number() {
		return destination_number;
	}

	public void setDestination_number(String destination_number) {
		this.destination_number = destination_number;
	}

	public String getUnit_name() {
		return unit_name;
	}

	public void setUnit_name(String unit_name) {
		this.unit_name = unit_name;
	}

	public MODE getMode() {
		return mode;
	}

	public void setMode(MODE mode) {
		this.mode = mode;
	}
	
	
}
