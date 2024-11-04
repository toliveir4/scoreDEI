# —————————— scoreDEI ——————————

Prerequisites for scoreDEI to function correctly:

	•	Install Docker Desktop;
	•	Have a browser installed;
	•	Appropriate software to view and edit “.java” and “.html” files, such as Visual Studio Code.

scoreDEI Platform Installation Guide:

	•	Open Docker Desktop;
	•	Open Visual Studio Code, or another IDE, in the source code folder (scoreDEI folder).
	•	Once the container loads completely, a pop-up will appear with the option “Reopen in Container”;
	•	In the console opened by the container, navigate to the folder with the Maven executable by running cd demoJPA+webservices;
	•	Run the command ./mvnw spring-boot:run;
	•	If an error occurs, delete the target folder inside the demoJPA+webservices folder and rerun the previous command.
	•	Open the link “http://localhost:8080/” in a browser.
