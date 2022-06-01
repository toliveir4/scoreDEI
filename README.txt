------------------------------ scoreDEI ------------------------------

-> Pré requisitos para o bom funcionamento do scoreDEI:
    - Instalar o Docker Desktop;
    - Ter um browser instalado;
    - Software adequado para visualizar e editar ficheiros ".java" e ".html", entre outros, tais como o Visual Studio Code.

-> Guia de instalação da plataforma scoreDEI:
    - Abrir o Docker Desktop;
    - Abrir o Visual Studio Code, ou outro IDE, na pasta do código fonte (pasta scoreDEI). 
    - Após o container carregar completamente irá aparecer um pop-up onde irá dizer "Reabrir no Container";
    - Na consola aberta pelo container, fazer "cd demoJPA+webservices" para ir para a pasta com o executável do Maven;
    - Correr o comando "./mvnw spring-boot:run";
    - Em caso de erro apagar a pasta target, que está localizada dentro da pasta demoJPA+webservices e voltar a correr o comando anterior.
    - Abrir num browser o link "http://localhost:8080/".
