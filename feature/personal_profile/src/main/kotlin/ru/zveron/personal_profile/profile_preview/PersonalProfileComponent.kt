package ru.zveron.personal_profile.profile_preview

import com.bumble.appyx.core.plugin.Destroyable
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.scope.Scope
import ru.zveron.personal_profile.profile_preview.data.PersonalProfileRepository

class PersonalProfileComponent: KoinScopeComponent, Destroyable {
    override val scope: Scope by lazy { createScope(this) }

    override fun destroy() {
        closeScope()
    }

    internal fun getPersonalProfileRepository(): PersonalProfileRepository {
        return scope.get()
    }
}