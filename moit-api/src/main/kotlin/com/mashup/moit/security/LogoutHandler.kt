package com.mashup.moit.security

import com.mashup.moit.common.exception.MoitException
import com.mashup.moit.common.exception.MoitExceptionType
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.stereotype.Controller
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import org.springframework.web.util.UriComponentsBuilder
import java.io.IOException

/**
 * Needed to perform SSO logout with Auth0. By default, Spring will clear the SecurityContext and the session.
 * This controller will also log users out of Auth0 by calling the Auth0 logout endpoint.
 */
@Controller
class LogoutHandler(
    /**
     * Create a new instance with a `ClientRegistrationRepository`, so that we can look up information about the
     * configured provider to call the Auth0 logout endpoint. Called by the Spring framework.
     *
     * @param clientRegistrationRepository the `ClientRegistrationRepository` for this application.
     */
    private val clientRegistrationRepository: ClientRegistrationRepository
) : SecurityContextLogoutHandler() {

    /**
     * Delegates to [SecurityContextLogoutHandler] to log the user out of the application, and then logs
     * the user out of Auth0.
     *
     * @param httpServletRequest  the request.
     * @param httpServletResponse the response.
     * @param authentication      the current authentication.
     */
    override fun logout(
        httpServletRequest: HttpServletRequest?, httpServletResponse: HttpServletResponse,
        authentication: Authentication?
    ) {

        // Invalidate the session and clear the security context
        super.logout(httpServletRequest, httpServletResponse, authentication)

        // Build the URL to log the user out of Auth0 and redirect them to the home page.
        // URL will look like https://YOUR-DOMAIN/v2/logout?clientId=YOUR-CLIENT-ID&returnTo=http://localhost:3000
        val issuer = clientRegistration.providerDetails.configurationMetadata["issuer"] as String?
        val clientId = clientRegistration.clientId
        val returnTo = ServletUriComponentsBuilder.fromCurrentContextPath().build().toString()
        val logoutUrl = UriComponentsBuilder
            .fromHttpUrl(issuer + "v2/logout?client_id={clientId}&returnTo={returnTo}")
            .encode()
            .buildAndExpand(clientId, returnTo)
            .toUriString()
        try {
            httpServletResponse.sendRedirect(logoutUrl)
        } catch (ioe: IOException) {
            throw MoitException.of(MoitExceptionType.AUTH_ERROR)
            // Handle or log error redirecting to logout URL
        }
    }

    private val clientRegistration: ClientRegistration
        /**
         * Gets the Spring ClientRegistration, which we use to get the registered client ID and issuer for building the
         * `returnTo` query parameter when calling the Auth0 logout API.
         *
         * @return the `ClientRegistration` for this application.
         */
        get() = clientRegistrationRepository.findByRegistrationId("auth0")

}
