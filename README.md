## CarMarket Backend

Spring Boot tabanlı “Car Market” backend API’si. PostgreSQL + Spring Security (JWT) kullanır. Araç/Marka yönetimi, sepet, favoriler ve fotoğraf yükleme uçları içerir.

### Teknolojiler
- Java 18 (Gradle toolchain)
- Spring Boot 3.5.3
- Spring Web, Spring Data JPA
- Spring Security + JWT (`Authorization: Bearer <token>`)
- PostgreSQL
- OpenAPI/Swagger UI (`springdoc-openapi`)
- Test: JUnit, RestAssured, Selenium (UI testleri mevcut)

### Proje Yapısı (özet)
- `src/main/java/com/example/springboot/controller/*` : REST controller’lar
- `src/main/java/com/example/springboot/service/*` : iş katmanı
- `src/main/java/com/example/springboot/repository/*` : JPA repository’ler
- `src/main/java/com/example/springboot/auth/*` : JWT + security config
- `src/main/resources/application.properties` : env tabanlı konfig

### Çalıştırma
Uygulama varsayılan olarak `10160` portundan çalışır.

#### Ortam Değişkenleri
`src/main/resources/application.properties` şu değişkenleri bekler:

- `DB_URL` (örn: `jdbc:postgresql://localhost:5432/carmarket`)
- `DB_USERNAME`
- `DB_PASSWORD`
- `JWT_SECRET_KEY`

> Not: Repo `.gitignore` içinde `.env` ignore’lanıyor. Lokal geliştirme için `.env` kullanabilirsin ama commit’leme.

### Güvenlik ve Kurulum Notları

#### Swagger / OpenAPI (endpoint keşfi)
Swagger UI ve `/v3/api-docs` endpoint’leri `permitAll()` yapılırsa, **tüm endpoint listesi ve şema detayları Swagger üzerinden görüntülenebilir**.  
Bu durum **tek başına** API’ye yazma yetkisi vermez; yazma işlemleri yine ilgili endpoint’lerin security kuralına (JWT/auth) bağlıdır.  
Prod ortamında internete açık bir deploy varsa, Swagger’ı public bırakmak yerine sadece `dev` profilinde açmanız önerilir.

#### Fotoğraf yükleme ve `/uploads`
Fotoğraflar diske yazılır ve `/uploads/**` üzerinden statik olarak servis edilir.

Mevcut kodda upload path’i **lokal makineye bağlı (hardcoded)** olduğu için projeyi başka bir bilgisayarda çalıştırırken:
- `UPLOAD_DIR` / upload klasörü yolu **kurulan ortama göre ayarlanmalıdır** (Windows kullanıcı yolu değişecektir).
- Üretim ortamında bu path’i hardcode etmek yerine environment variable / `application.properties` ile konfigüre etmek önerilir.

#### Windows (PowerShell) örnek 
```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/carmarket"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="postgres"
$env:JWT_SECRET_KEY="change-me-to-a-strong-secret"

.\gradlew.bat bootRun

  
