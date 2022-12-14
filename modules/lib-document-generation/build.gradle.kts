val jjwtVersion: String by rootProject.extra

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("io.jsonwebtoken:jjwt:${jjwtVersion}")
    implementation("joda-time:joda-time:2.10.13")

    //read docx
    implementation("org.apache.poi:poi:3.10.1")
    implementation("org.apache.poi:poi-ooxml:3.10.1")
    //docx to pdf
    implementation("org.apache.poi:ooxml-schemas:1.1")
    implementation("fr.opensagres.xdocreport:xdocreport:1.0.6")
    implementation("com.lowagie:itext:2.1.7")
    //pdf
    implementation("com.itextpdf:itextpdf:5.2.0")
    implementation("com.itextpdf:itext-asian:5.2.0")

    implementation(project(":lib-exceptions"))
    implementation(project(":lib-document-template"))
    api(project(":lib-base-jpa"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.h2database:h2:1.4.200")
}