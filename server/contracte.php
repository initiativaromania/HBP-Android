<?php
set_time_limit ( 0 );
iconv_set_encoding('internal_encoding', 'UTF-8');
mb_internal_encoding('UTF-8');
$res = new mysqli('localhost', 'root', '', 'ir-investitii');

$row = 1;

if (($handle = fopen("contracts_final.csv", "r")) !== FALSE) {
	while (($data = fgetcsv($handle, 1000, ",")) !== FALSE) {

		if(!empty($data[1])) {

			$query = "INSERT INTO  `ir-investitii`.`contracte` (
				`id` ,
				`contract_title` ,
				`address` ,
				`location_lat` ,
				`location_lng` ,
				`company` ,
				`start_date` ,
				`categories` ,
				`price` ,
				`currency` ,
				`justify` ,
				`CPVCodeID` ,
				`CPVCode` ,
				`contract_nr` ,
				`buyer`
			)
			VALUES (
				NULL , '".mysqli_escape_string($res, $data[1])."', '".mysqli_escape_string($res, $data[2])."',  '{$data[3]}',  '{$data[4]}', '".mysqli_escape_string($res, $data[5])."',  '{$data[6]}', 
				'{$data[7]}', '{$data[8]}',  '{$data[9]}',  '{$data[10]}',  '{$data[11]}', 
				'{$data[12]}',  '{$data[13]}',  '".mysqli_escape_string($res, $data[14])."'
			);
			";

			mysqli_query($res, $query);
			//echo mysqli_error($res);exit;

		}

	}
}
fclose($handle);
