plugins {
    id("java-library")
    id("kotlin")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_7
    targetCompatibility = JavaVersion.VERSION_1_7
}
dependencies {
    implementation("org.jsoup:jsoup:1.14.1")
    api("com.googlecode.json-simple:json-simple:1.1.1")

}