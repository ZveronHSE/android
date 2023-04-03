package ru.zveron.authorization.socials_sheet

import android.content.Context
import net.openid.appauth.AppAuthConfiguration
import net.openid.appauth.AuthorizationService
import net.openid.appauth.browser.BrowserAllowList
import net.openid.appauth.browser.VersionedBrowserMatcher
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.module
import ru.zveron.authorization.socials_sheet.data.LoginBySocialsRepository
import ru.zveron.authorization.socials_sheet.domain.GoogleAuthInteractor
import ru.zveron.authorization.socials_sheet.domain.LoginBySocialsInteractor

val socialsModule = module {
    scope<SocialsComponent> {
        viewModelOf(::SocialsViewModel)
        scopedOf(::GoogleAuthInteractor)

        scoped {
            val applicationContext = get<Context>()
            val appAuthConfiguration = AppAuthConfiguration.Builder()
                .setBrowserMatcher(
                    BrowserAllowList(
                        VersionedBrowserMatcher.CHROME_CUSTOM_TAB,
                        VersionedBrowserMatcher.FIREFOX_CUSTOM_TAB,
                        VersionedBrowserMatcher.SAMSUNG_CUSTOM_TAB,
                    )
                )
                .build()

            AuthorizationService(applicationContext, appAuthConfiguration)
        }

        scopedOf(::LoginBySocialsInteractor)
        scopedOf(::LoginBySocialsRepository)
    }
}