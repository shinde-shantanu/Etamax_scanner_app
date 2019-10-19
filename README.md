# Etamax_scanner_app
The Scanner app was used during the annual tech fest at FCRIT by the student council to scan the barcodes for event registration for payment. This application was used to scan barcodes generated as bills for all registrations for all events conducted in Etamax 2019.

This android based applicationwas buit using Androd Studios.
It makes use of google APIs for barcode detection.

There are to major activities-
	1. The Main Activity-
		This activity contains the barcode scanner. It scans the gerated barcode and sends a JSON object containing the name, branch, roll number, id of the student along with the amount to the server.
	
	2. Another activity to display-
		This activity displays the name, branch, roll number, id and the cost of the student which it recieves in JSON object from the server.