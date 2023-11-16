import kotlin.jvm.internal.CallableReference
import kotlin.reflect.KProperty0

plugins {
    `maven-publish`
    id("net.idlestate.gradle-duplicate-classes-check")
}

val scijava = project.extensions.create<ScijavaPublishExtension>("scijava")

typealias OrganizationProp = KProperty0<Property<ScijavaPublishExtension.Organization>>
typealias LicenceProp = KProperty0<Property<ScijavaPublishExtension.License>>

afterEvaluate {
    publishing {
        publications.create<MavenPublication>("scijava") {

            fun <T> KProperty0<Property<T>>.getOrThrow(): T {
                val prop = get()
                val prefix = (this as CallableReference).owner.toString().substringAfter('$', "")
                val fullName = if (prefix.isEmpty()) name else "${prefix.lowercase()}.$name"
                return if (prop.isPresent) prop.get() else throw IllegalStateException("Property `$fullName` is required")
            }

            fun <T> Provider<T>.ifPresent(block: (T) -> Unit) {
                if (isPresent)
                    block(get())
            }

            groupId = scijava::group.getOrThrow()
            artifactId = scijava::artifact.getOrThrow()
            version = scijava::version.getOrThrow()

            pom {
                name = scijava::name.getOrThrow()
                description = scijava::description.getOrThrow()
                url = scijava::url.getOrThrow().toString()
                inceptionYear = scijava::inceptionYear.getOrThrow().toString()
                organization {
                    val prop: OrganizationProp = scijava::organization
                    val org = prop.getOrThrow()
                    name = org::name.getOrThrow()
                    url = org::url.getOrThrow().toString()
                }
                licenses {
                    license {
                        val prop: LicenceProp = scijava::license
                        val lic = prop.getOrThrow()
                        name = lic::name.getOrThrow()
                        url = lic::url.getOrThrow().toString()
                    }
                }
                developers {
                    val devs = scijava.developers.get()
                    require(devs.isNotEmpty()) { "At least one developer must be defined "}
                    for (dev in devs)
                        developer {
                            id = dev::nick.getOrThrow()
                            name = dev::name.getOrThrow()
                            email = dev::email.getOrThrow()
                            dev.url.ifPresent { url = it.toString() }
                            dev.organization.ifPresent { organization = it }
                            dev.organizationUrl.ifPresent { organizationUrl = it.toString() }
                            dev.roles.ifPresent { p -> roles = p.map { it.name } }
                            dev.timezone.ifPresent { timezone = it.displayName }
                            dev.properties.ifPresent { p -> properties = p.mapValues { it.value.toString() } }
                        }
                }
                contributors {
                    val cons = scijava.contributors.get()
                    require(cons.isNotEmpty()) { "At least one contributor must be defined "}
                    for (con in cons)
                        contributor {
                            name = con::name.getOrThrow()
                            email = con::email.getOrThrow()
                            con.url.ifPresent { url = it.toString() }
                            con.organization.ifPresent { organization = it }
                            con.organizationUrl.ifPresent { organizationUrl = it.toString() }
                            con.roles.ifPresent { p -> roles = p.map { it.name } }
                            con.timezone.ifPresent { timezone = it.displayName }
                            con.properties.ifPresent { p -> properties = p.mapValues { it.value.toString() } }
                        }
                }
                mailingLists {
                    mailingList {
                        val ml = scijava.mailingList.get()
                        name = ml.name
                        subscribe = ml.subscribe
                        unsubscribe = ml.unsubscribe
                        post = ml.post
                        archive = ml.archive.get().toString()
                        otherArchives = ml.otherArchives.get().map { it.toString() }
                    }
                }
//                scm {
//                    val scm = scijava.smc.get()
//                    connection = scm.connection.get().toString()
//                    developerConnection = scm.developerConnection.get().toString()
//                    url = scm.url.get().toString()
//                    tag = scm.tag.get()
//                }
//                issueManagement {
//                    val im = scijava.issueManagement.get()
//                    system = im.system.get()
//                    url = im.url.get().toString()
//                }
//                ciManagement {
//                    val cm = scijava.ciManagement.get()
//                    system = cm.system.get()
//                    url = cm.url.get().toString()
//                }
            }
        }
        repositories {

        }
    }
}

