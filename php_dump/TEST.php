<?php


// PHP
ini_set('display_errors', 'On');
error_reporting(E_ALL);

// CONN PARAMS
$COM_PORT = "com5";
$SMS_DEST_NUMBER = "0754111222";

// SMS PARAMS
$SMS_BODY = "SQQPC;RESTART;.....;0000;SQQPC";

//exec("mode com5: BAUD=9600 PARITY=n DATA=8 STOP=1 to=off dtr=off rts=off");
echo "Opening serial connection to COM: " . $COM_PORT . "<br>";

exec("mode com5: BAUD=9600 PARITY=n DATA=8 STOP=1 to=off dtr=off rts=off");
$serial_conn =fopen($COM_PORT, "w");

// CONNECTION HANDLING
if(!$serial_conn)
	die("Serial connection to COM: " . $COM_PORT . " could not be established... Exiting.");

echo "Sending SMS: " .  $SMS_BODY . " to destination number: " . $SMS_DEST_NUMBER . "<br>";
fwrite($serial_conn, "AT+CMGF=1\r");
fwrite($serial_conn, "AT+CMGS=\"" . $SMS_DEST_NUMBER . "\"\r" . $SMS_BODY .  chr(26));
echo "SMS Sent!<br>";
fclose($serial_conn);

die("<br>Process ended...");
?>


