<?php

error_reporting(E_ALL);
ini_set('display_errors', '1');

$number = $_GET["n"];
$name = $_GET["un"];
$mode = $_GET["m"];

$valid = false;

echo "Sending SMS...<br>";
echo "To number: " . $number . "<br>";
echo "Unit name: " . $name . "<br>";
if($mode === "keep"){
	echo "Mode: restart (keep tasks)<br>";
	$valid = true;
}
if($mode === "del"){
	echo "Mode: restart (delete tasks)<br>";
	$valid = true;
}

	
if($valid){
	$WshShell = new COM("WScript.Shell");	
	$obj = $WshShell->Run("SMS\putty.exe -serial com5", 5, false);
	echo $obj;
	$WshShell->appactivate("PuTTy");
	
	$message = null;
	
	if($mode === "keep")
		$message = "SQQPC;RESTART;" . $name . ";0000;SQQPC";
	else IF($mode === "del")
		$message = "SQQPC;REBOOT;" . $name . ";0000;SQQPC";


	sleep(1);

  $WshShell->SendKeys("AT{+}CMGF=1{ENTER}");
  $WshShell->SendKeys("AT{+}CMGS=");
  $WshShell->SendKeys("\"" . $number  ."\"{ENTER}");
  $WshShell->SendKeys($message);
  sleep(1);
  $WshShell->SendKeys("^{z}");
  sleep(1);
  $WshShell->SendKeys("%{F4}" . "{ENTER}");
	
	
	echo "<br>Sent";
}else 
	echo "Wrong mode argument.<br>Message has not been sent.";

?>