import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.newInstance
import java.net.URL
import java.util.*
import javax.inject.Inject

interface ScijavaPublishExtension {

    @get:Inject
    val objects: ObjectFactory

    val group: Property<String>
    val artifact: Property<String>
    val version: Property<String>

    var gav: String
        get() = "$group:$artifact:$version"
        set(value) {
            val (g, a, v) = value.split(":")
            group = g
            artifact = a
            version = v
        }

    val name: Property<String>
    val description: Property<String>
    val url: Property<URL>
    val inceptionYear: Property<Int>
    val organization: Property<Organization>

    fun organization(action: Action<Organization>) {
        organization = objects.newInstance<Organization>()
        action.execute(organization.get())
    }

    interface Organization {
        val name: Property<String>
        val url: Property<URL>
    }

    val license: Property<License>

    fun license(action: Action<License>) {
        license = objects.newInstance<License>()
        action.execute(license.get())
    }

    interface License {
        val name: Property<String>
        val url: Property<URL>
        val distribution: Property<String>
        val comments: Property<String>
        val copyrightOwners: SetProperty<String>
    }

    val developers: ListProperty<Developer>

    fun developer(action: Action<Developer>) = action.execute(objects.newInstance<Developer>().also(developers::add))

    interface Developer : Contributor {
        val nick: Property<String>
    }

    val contributors: ListProperty<Contributor>

    fun contributor(action: Action<Contributor>) = action.execute(objects.newInstance<Contributor>().also(contributors::add))
    interface Contributor {

        val name: Property<String>
        val email: Property<String>
        val url: Property<URL>
        val organization: Property<String>
        val organizationUrl: Property<URL>
        val roles: SetProperty<Role>
        val timezone: Property<TimeZone>
        val properties: MapProperty<String, Any>

        enum class Role {
            /** Created the project. Does not imply any current participation or responsibility. */
            Founder,

            /** Has decision-making authority: timing of releases, inclusion of features, etc. */
            Lead,

            /** Adds new features or enhancements. Can be assigned to address feature requests. */
            Developer,

            /** Fixes bugs. Can be assigned open issues to solve. */
            Debugger,

            /** Reviews patch submissions. */
            Reviewer,

            /** Responds to community questions and issue reports. Keeps the issue tracker organized. */
            Support,

            /** Merges patch submissions. Cuts releases. */
            Maintainer,

            /** Contributed code to the project. Does not imply any current participation or responsibility. */
            Contributor
        }
    }

    val mailingList: Property<MailingList>

    fun mailingList(action: Action<MailingList>) {
        mailingList = objects.newInstance<MailingList>()
        action.execute(mailingList.get())
    }

    interface MailingList {
        val name: Property<String>
        val subscribe: Property<String>
        val unsubscribe: Property<String>
        val post: Property<String>
        val archive: Property<URL>
        val otherArchives: SetProperty<URL>
    }

    val smc: Property<Scm>

    fun smc(action: Action<Scm>) = action.execute(smc.get())

    interface Scm {
        val connection: Property<URL>
        val developerConnection: Property<URL>
        val url: Property<URL>
        val tag: Property<String>
    }

    val issueManagement: Property<Management>

    fun issueManagement(action: Action<Management>) = action.execute(issueManagement.get())

    val ciManagement: Property<Management>

    fun ciManagement(action: Action<Management>) = action.execute(ciManagement.get())

    interface Management {
        val system: Property<String>
        val url: Property<URL>
    }
}