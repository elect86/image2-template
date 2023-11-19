import kotlin.jvm.internal.CallableReference
import kotlin.reflect.KProperty0

plugins {
    `maven-publish`
    id("net.idlestate.gradle-duplicate-classes-check")
}

val scijava = project.extensions.create<ScijavaPublishExtension>("scijava")

afterEvaluate {
    publishing {
        publications.create<MavenPublication>("scijava") {

            fun <T> KProperty0<Provider<T>>.getOrThrow(): T {
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
                        properties = properties.get() + ("copyrightOwners" to lic::copyrightOwners.getOrThrow().joinToString())
                    }
                }
                developers {
                    val devs = scijava.developers.get()
                    require(devs.isNotEmpty()) { "At least one developer must be defined "}
                    for (dev in devs)
                        developer {
                            id = dev::nick.getOrThrow()
                            name = dev::name.getOrThrow()
                            dev.email.ifPresent { email = it }
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
                            con.email.ifPresent { email = it }
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
                        val m: MailingProp = scijava::mailingList
                        val ml = m.getOrThrow()
                        name = ml::name.getOrThrow()
                        ml.subscribe.ifPresent { subscribe = it }
                        ml.unsubscribe.ifPresent { unsubscribe = it }
                        ml.post.ifPresent { post = it }
                        archive = ml::archive.getOrThrow().toString()
                        ml.otherArchives.ifPresent { p -> p.map { it.toString() } }
                    }
                }
                scm {
                    val s: ScmProp = scijava::smc
                    val scm = s.getOrThrow()
                    connection = scm::connection.getOrThrow().toString()
                    scm.developerConnection.ifPresent { developerConnection = it.toString() }
                    url = scm::url.getOrThrow().toString()
                    scm.tag.ifPresent { tag = it }
                }
                issueManagement {
                    val i: ManProp = scijava::issueManagement
                    val im = i.getOrThrow()
                    system = im::system.getOrThrow()
                    url = im::url.getOrThrow().toString()
                }
                ciManagement {
                    val c: ManProp = scijava::ciManagement
                    val cm = c.getOrThrow()
                    system = cm::system.getOrThrow()
                    url = cm::url.getOrThrow().toString()
                }
            }
        }
        repositories {

        }
    }
}

typealias OrganizationProp = KProperty0<Property<ScijavaPublishExtension.Organization>>
typealias LicenceProp = KProperty0<Property<ScijavaPublishExtension.License>>
typealias MailingProp = KProperty0<Property<ScijavaPublishExtension.MailingList>>
typealias ScmProp = KProperty0<Property<ScijavaPublishExtension.Scm>>
typealias ManProp = KProperty0<Property<ScijavaPublishExtension.Management>>

