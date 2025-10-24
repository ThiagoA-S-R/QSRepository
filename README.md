# 🧑‍💻 AppFullstackQAUsuarios

Aplicação Full Stack simples para **cadastro de usuários**, utilizando:
- **Spring Boot (Java 17)** no backend  
- **Banco H2 em memória**  
- **HTML, CSS e JavaScript** no frontend (pasta `src/main/resources/static`)  

---

## 🚀 Funcionalidades

- Criar, listar e excluir usuários  
- Banco de dados em memória (H2)  
- API REST com Spring Boot  
- Front-end simples em HTML/JS  
- Teste de conexão com H2 Console  

---

## 🛠️ Requisitos

- [Java 17+](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)  
- [Maven 3.9+](https://maven.apache.org/download.cgi)

---

## ⚙️ Como rodar o projeto

Abra o terminal na pasta do projeto (onde está o `pom.xml`) e execute os seguintes comandos:

### 1️⃣ Limpar e instalar dependências
```bash
mvn clean install -DskipTests
```

### 2️⃣ Rodar a aplicação
```bash
mvn spring-boot:run
```

A aplicação será iniciada em:
👉 **http://localhost:8080**

---

## 🧩 Banco de dados H2

A aplicação utiliza um banco de dados H2 em memória (não persiste após reiniciar).  

Para acessar o console H2, abra no navegador:
👉 **http://localhost:8080/h2-console**

### Configurações de conexão:
| Campo | Valor |
|-------|-------|
| JDBC URL | `jdbc:h2:mem:testdb` |
| User Name | `sa` |
| Password | *(deixe em branco)* |

Depois clique em **Connect**.

---

## 🧠 Estrutura do Projeto

```
src
├── main
│   ├── java
│   │   └── com.example.usuarios
│   │       ├── controller   # Controladores REST
│   │       ├── model        # Entidades (User.java)
│   │       ├── repository   # Interfaces JPA
│   │       └── AppFullstackQaUsuariosApplication.java
│   └── resources
│       ├── static           # Frontend (HTML, CSS, JS)
│       └── application.properties
└── test
    └── java                # Testes automatizados
```

---

## 🌐 Frontend

Após iniciar a aplicação (`mvn spring-boot:run`), abra no navegador:

👉 [http://localhost:8080](http://localhost:8080)

Você verá a tela de **Cadastro de Usuários**, onde pode:
- Inserir nome, email e idade  
- Clicar em **Adicionar**  
- Visualizar a lista de usuários  
- Excluir registros  

---

## 🧾 Comandos úteis

### Parar a aplicação
Pressione `Ctrl + C` no terminal.

### Limpar e recompilar o projeto
```bash
mvn clean package -DskipTests
```

### Rodar novamente
```bash
mvn spring-boot:run
```

---

## 💡 Observações

- O banco de dados H2 é **temporário**, ou seja, ao reiniciar o servidor os dados são perdidos.
- Se quiser usar um banco real (MySQL, PostgreSQL etc.), basta ajustar o arquivo `application.properties`.

---

## 👨‍💻 Autor

**Thiago Anastácio**  
Projeto desenvolvido como parte de estudos em **Spring Boot + Frontend básico com JS**.
