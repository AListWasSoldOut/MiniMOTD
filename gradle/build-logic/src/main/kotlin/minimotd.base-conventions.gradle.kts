plugins {
  id("java-library")
  id("net.kyori.indra")
  id("net.kyori.indra.git")
  id("net.kyori.indra.checkstyle")
  id("net.kyori.indra.licenser.spotless")
}

version = (version as String)
  .run { if (this.endsWith("-SNAPSHOT")) "$this+${lastCommitHash()}" else this }

indra {
  javaVersions {
    minimumToolchain(17)
    target(8)
  }
  github(Constants.GITHUB_USER, Constants.GITHUB_REPO)
  mitLicense()
}

repositories {
  mavenCentral {
    mavenContent { releasesOnly() }
  }
  maven("https://repo.jpenilla.xyz/snapshots/") {
    mavenContent {
      snapshotsOnly()
      includeGroup("xyz.jpenilla")
      includeGroup("net.kyori") // TODO adventure-platform and adventure-platform-mod snapshots
    }
  }
  maven("https://central.sonatype.com/repository/maven-snapshots/") {
    mavenContent { snapshotsOnly() }
  }
  maven("https://repo.papermc.io/repository/maven-public/")
  maven("https://repo.spongepowered.org/repository/maven-public/")
}

dependencies {
  testImplementation(libs.jupiterEngine)
  // TODO: work around archloom issue?
  if (project.name != "minimotd-neoforge" && project.name != "minimotd-fabric") {
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
  }
}

tasks {
  withType<JavaCompile> {
    options.compilerArgs.add("-Xlint:-processing")
  }
  listOf(javadocJar, javadoc).forEach {
    it.configure {
      onlyIf { false }
    }
  }
  test {
    testLogging {
      events("passed")
    }
  }
}
