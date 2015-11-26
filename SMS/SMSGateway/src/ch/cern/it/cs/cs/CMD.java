package ch.cern.it.cs.cs;

import java.io.IOException;
import java.io.PrintWriter;

import ch.cern.it.cs.cs.assets.SyncPipe;

public class CMD {

	private static String CMD[] = { "cmd" };

	private Process CMD_PROCESS;
	private PrintWriter stdin;

	public void startCMD() {
		try {
			CMD_PROCESS = Runtime.getRuntime().exec(CMD);
			new Thread(new SyncPipe(CMD_PROCESS.getErrorStream(), System.err)).start();
			new Thread(new SyncPipe(CMD_PROCESS.getInputStream(), System.out)).start();
			stdin = new PrintWriter(CMD_PROCESS.getOutputStream());
			stdin.println("cd c:\\\\inetpub\\wwwroot\\SMS");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void SEND_SMS(QP_SMS sms) {
		CMD cmd = new CMD();
		cmd.startCMD();
		cmd.sendQPSMS(sms);
		cmd.stopCMD();
	}

	public void sendQPSMS(QP_SMS sms) {
		String execution_cmd = "SMS.vbs ";
		if (sms != null) {
			execution_cmd += sms.getDestination_number() + " ";
			execution_cmd += sms.getUnit_name() + " ";

			if (sms.getMode().toString() == QP_SMS.MODE.DELETE_TASKS.toString())
				execution_cmd += "del";
			else if (sms.getMode().toString() == QP_SMS.MODE.KEEP_TASKS.toString())
				execution_cmd += "keep";
			else if (sms.getMode().toString() == QP_SMS.MODE.APN_RESET.toString()) 
				execution_cmd += "apn_reset";
			else if (sms.getMode().toString() == QP_SMS.MODE.APN_RESET.toString()) 
				execution_cmd += "ftp_reset";
			

			stdin.println(execution_cmd);

			System.out.println("-------------\nSMS sent to: " + sms.getDestination_number() + "\nUnit Name: "
					+ sms.getUnit_name() + "\nMode: " + sms.getMode().toString() + "\n-------------\n");
		} else {
			System.err.println("Error sending SMS - (null)");
		}
	}

	public void stopCMD() {
		int returnCode = -1;
		try {
			stdin.close();
			returnCode = CMD_PROCESS.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Return code = " + returnCode);
	}

}
