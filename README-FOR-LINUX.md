# Documentação: Corrigindo o Maven Wrapper (`mvnw`) em projetos vindos do Windows para Linux

## Contexto do Problema

Quando um projeto Java é desenvolvido no **Windows** e depois clonado ou transferido para **Linux/Debian**, é comum encontrar problemas com o **Maven Wrapper** (`mvnw`).

O **erro mais típico** é:

```
./mvnw clean install
# ou
pede permissão mas ao dar permissão tipo sudo ele não executa.
```

ou, em casos mais sutis:

* Nada acontece ao executar `./mvnw`.
* Mensagens de erro confusas, mesmo estando na raiz do projeto.

---

## Por que isso acontece

### 🔹 Diferenças entre Windows e Linux

| Aspecto               | Windows                            | Linux/Debian                     |
| --------------------- | ---------------------------------- | -------------------------------- |
| Sistema de arquivos   | Case-insensitive (`mvnw` = `MVNW`) | Case-sensitive (`mvnw` ≠ `MVNW`) |
| Quebra de linha       | CRLF (`\r\n`)                      | LF (`\n`)                        |
| Permissão de execução | Não obrigatória                    | Obrigatória (`chmod +x`)         |
| Scripts               | `.bat` ou `.cmd` para Windows      | Shell script `.sh` (`mvnw`)      |

**Consequência:** ao copiar arquivos do Windows para Linux:

1. Scripts podem ter **quebra de linha CRLF**, fazendo o shell ignorar o conteúdo.
2. Arquivos podem estar **vazios, corrompidos ou configurados apenas para Windows** (como `.bat`/`.cmd`).
3. Se o arquivo não tiver permissão de execução, o Linux não consegue rodá-lo.

---

### 🔹 Caso específico do projeto

* Havia arquivos do Maven Wrapper, mas eles estavam configurados **para rodar apenas no Windows** (como `mvnw.cmd`), e o `mvnw` do Linux estava **vazio**.

* Ao tentar executar `./mvnw clean install` na raiz do projeto:

  * Não havia script Linux válido → nada aconteceu.
  * Rodar em `.mvn/wrapper` causava erro de “Missing POM”, porque o Maven procurava o `pom.xml` no diretório errado.

* Problemas típicos de Windows (CRLF, permissões) também podem aparecer em outros projetos, mas **o principal aqui foi que o script Linux não existia**.

---

## Comparação do comportamento do Maven Wrapper

### 🔹 No Windows

* O Maven Wrapper é executado via `mvnw.cmd`.
* O script chama o Maven global ou baixa a versão necessária.
* Não é sensível a permissões ou quebras de linha, então mesmo scripts com CRLF funcionam.

### 🔹 No Linux/Debian

* O Maven Wrapper é executado via `./mvnw` (shell script).

* Precisa de:

  1. Permissão de execução (`chmod +x mvnw`)
  2. Quebras de linha corretas (`LF`)
  3. Arquivo não vazio com conteúdo correto

* Scripts vazios ou com CRLF **não funcionam**, podendo não dar erro algum.

---

## Passo a passo de correção

### **Passo 1 — Remover arquivos antigos problemáticos**

```bash
cd .mvn/wrapper
ls -lQ
rm -f mvnw
cd ../..
```

---

### **Passo 2 — Criar o script `mvnw` manualmente**

```bash
nano mvnw
```

Cole o seguinte conteúdo:

```bash
#!/bin/sh
# Maven Wrapper script manual (Debian/Linux)

MAVEN_VERSION=3.9.11
MAVEN_HOME="$HOME/.m2/wrapper/apache-maven-$MAVEN_VERSION"
MAVEN_DIST_URL="https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/$MAVEN_VERSION/apache-maven-$MAVEN_VERSION-bin.zip"

# Baixa e extrai Maven se não existir
if [ ! -d "$MAVEN_HOME" ]; then
  echo "Maven $MAVEN_VERSION não encontrado. Baixando..."
  mkdir -p "$MAVEN_HOME"
  curl -L "$MAVEN_DIST_URL" -o /tmp/maven.zip
  unzip -q /tmp/maven.zip -d /tmp
  mv /tmp/apache-maven-$MAVEN_VERSION/* "$MAVEN_HOME"
  rm -rf /tmp/apache-maven-$MAVEN_VERSION /tmp/maven.zip
fi

# Executa Maven com os argumentos passados
"$MAVEN_HOME/bin/mvn" "$@"
```

---

### **Passo 3 — Dar permissão de execução**

```bash
chmod +x mvnw
```

---

### **Passo 4 — Rodar o Maven Wrapper**

Na raiz do projeto (onde está o `pom.xml`):

```bash
./mvnw clean install
```

* Na primeira execução, o Maven será baixado automaticamente e armazenado em `~/.m2/wrapper/apache-maven-3.9.11`.
* Em execuções futuras, o Maven será usado diretamente do cache.

---

### **Passo 5 — Executar um projeto Spring Boot**

Se o projeto for **Spring Boot**, você pode iniciar a aplicação diretamente com:

```bash
./mvnw spring-boot:run
```

* Isso irá compilar e executar o projeto usando o Maven Wrapper configurado.
* Garantindo que a mesma versão do Maven usada no projeto será usada, sem depender de instalação global.

---

## Dicas adicionais

* Sempre verifique o **conteúdo do `mvnw`** após transferir projetos do Windows:

```bash
file mvnw
```

* Se mostrar `CRLF`, use:

```bash
dos2unix mvnw
```

* Sempre execute o Maven **da raiz do projeto** (onde está o `pom.xml`).

* Nunca execute scripts da pasta `.mvn/wrapper` — essa pasta serve apenas para armazenar o `maven-wrapper.properties` e outros arquivos internos.

---

## Resumo

* Problema: `./mvnw` não funcionava porque o script Linux estava **vazio**, enquanto o projeto tinha arquivos apenas para Windows.
* Causa: diferença de sistemas (Windows vs Linux) e transferência de arquivos.
* Solução: criar manualmente o `mvnw` shell script funcional, com permissão de execução e download automático do Maven.
* Bônus: após correção, é possível **executar projetos Spring Boot** diretamente com `./mvnw spring-boot:run`.


