# Clinix Scheduling Service

Clinix Scheduling Service é um microserviço responsável pelo gerenciamento de agendamentos de consultas médicas na plataforma Clinix. Ele permite a criação, atualização, cancelamento e exclusão de agendamentos, integrando-se a outros serviços via RMI e utilizando Redis para cache.

## 📌 Funcionalidades
- Listagem de todos os agendamentos
- Busca de agendamento por ID
- Criação de novos agendamentos
- Atualização do status do agendamento
- Cancelamento de agendamentos com motivo
- Exclusão de agendamentos

## 🛠 Tecnologias Utilizadas
- **Java 21**
- **Spring Boot 3** (Data JPA, Web, Test, Validation, Actuator, Cache)
- **Tomcat**
- **Lombok**
- **Micrometer Registry Prometheus**
- **PostgreSQL** (Banco de Dados Relacional)
- **Redis** (Cache)
- **Mockito & JUnit** (Testes)
- **Docker** (Containerização)

## 🚀 Instalação e Configuração

### 🔹 Pré-requisitos
- Java 21+
- Docker e Docker Compose
- PostgreSQL
- Redis

### 🔹 Passo a Passo

1. Clone o repositório:
   ```sh
   git clone https://github.com/seu-usuario/clinix-scheduling-service.git
   cd clinix-scheduling-service
   ```

2. Configure o banco de dados no `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/clinix_scheduling
   spring.datasource.username=postgres
   spring.datasource.password=postgres
   spring.jpa.hibernate.ddl-auto=update
   ```

3. Suba os containers do PostgreSQL e Redis:
   ```sh
   docker-compose up -d
   ```

4. Execute o serviço:
   ```sh
   mvn spring-boot:run
   ```

### Exemplo de Requisição
#### Criar um agendamento (POST `/appointments`)
```json
{
  "doctorId": 1,
  "patientId": 2,
  "clinicId": 3,
  "dateTime": "2025-04-01T10:00:00",
  "status": "PENDENTE"
}
```

## ✅ Testes
Para executar os testes unitários, rode:
```sh
mvn test
```

## 📜 Contribuição
1. Crie um fork do projeto
2. Crie uma branch (`git checkout -b feature-minha-feature`)
3. Faça commit das mudanças (`git commit -m 'Adiciona minha feature'`)
4. Envie para o repositório remoto (`git push origin feature-minha-feature`)
5. Abra um Pull Request 🚀

## 📄 Licença
Este projeto está sob a licença MIT. Sinta-se à vontade para utilizá-lo e contribuir!

---

💡 **Dúvidas? Sugestões?** Abra uma issue! 🎯
