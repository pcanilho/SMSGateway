


Dim wshShell
Dim intCount
Dim number
Dim name
Dim mode
Dim message

' GET WScript SHELL
Set wshShell= CreateObject("WScript.Shell")

' READ ARGUMENTS
number = WScript.ARGUMENTS.Item(0)
name = WScript.ARGUMENTS.Item(1)
mode = WScript.ARGUMENTS.Item(2)

Dim exit_cmd 
exit_cmd = "Exit"

IF mode = "keep" THEN
  message = "SQQPC;RESTART;" + name + ";0000;SQQPC"
END IF
IF mode = "del"	THEN
  message = "SQQPC;REBOOT;" + name + ";0000;SQQPC"
END IF
IF mode = "apn_reset"	THEN
  message = "SQQPC;AP-1;0001;1=CERN internal APN;8=8080;3=GPRS;20=*99#;40=cern.swisscom.ch;SQQPC"
END IF
IF mode = "ftp_reset"	THEN
  message = "SQQPC;POLL;137.138.34.207;QNCERN;NQFTP!cce!!.cern;;" + name + ";CERN internal APN;0000;1;SQQPC"
END IF

'message = "SQQPC;AP-1;0001;1=CERN internal APN;8=8080;3=GPRS;20=*99#;40=cern.swisscom.ch;SQQPC"
'apn_message = "SQQPC;AP-1;0001;1=CERN internal APN;8=8080;3=GPRS;20=*99#;40=cern.swisscom.ch;SQQPC"
'ftp_message = "SQQPC;POLL;137.138.34.207;QNCERN;NQFTP!cce!!.cern;;LHC7;CERN internal APN;0000;1;SQQPC"

'Open PuTTY

x = wshShell.Run("putty.exe -serial com5", vbHidden)
'wshShell.Run "putty.exe -serial com5"

'Wait a bacalhau
WScript.sleep 100

intCount=0

'Send the keys to notepad application 
  wshShell.SendKeys "AT{+}CMGF=1{ENTER}" 
  wshShell.SendKeys "AT{+}CMGS="
  wshShell.SendKeys """"+ number +"""{ENTER}"
  wshShell.SendKeys message
 'WScript.sleep 1
  wshShell.SendKeys "^{z}"
 'WScript.sleep 1
  wshShell.SendKeys exit_cmd + "{ENTER}"	
 'WScript.sleep 1
  wshShell.SendKeys "%{F4}" + "{ENTER}"
  

'Quit
WScript.Quit