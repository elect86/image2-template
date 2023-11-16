import java.net.URL
import java.time.ZoneOffset
import java.util.*

plugins {
    embeddedKotlin("jvm")
    `scijava-publish`
}

//apply<ScijavaPublish>()

repositories {
    mavenCentral()
}

//configure<ScijavaPublishExtension> {
//
//}




scijava {
    group = "group"
    artifact = "artifact"
    version = "version"
    name = "name"
    description = "descr"
    url = URL("https://github.com/scijava/pom-scijava-base#enforcer-rules-declared-in-this-parent")
    inceptionYear = 1970
    organization {
        name = "organization name"
        url = URL("https://example.com/organization")
    }
    license {
        name = "license name"
        url = URL("https://example.com/license")
    }
    developer {
        name = "developer name"
        nick = "developer nick"
        email = "developer email"
        url = URL("https://example.com/developer")
        organization = "developer organization"
        organizationUrl = URL("https://example.com/developer-organization")
        roles.add(ScijavaPublishExtension.Contributor.Role.Developer)
        timezone = TimeZone.getTimeZone(ZoneOffset.UTC)
        properties = mapOf("key" to "value")
    }
    contributor {
        name = "contributor name"
        email = "contributor email"
        url = URL("https://example.com/contributor")
        organization = "contributor organization"
        organizationUrl = URL("https://example.com/contributor-organization")
        roles.add(ScijavaPublishExtension.Contributor.Role.Contributor)
        timezone = TimeZone.getTimeZone(ZoneOffset.UTC)
        properties = mapOf("key" to "value")
    }

}