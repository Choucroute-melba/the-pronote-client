package moi.choucroutemelba.thepronoteclient.data.pronote

import android.util.Log
import android.webkit.*
import androidx.compose.ui.text.toUpperCase

val tag = "data/pronote/client"

class PronoteWebViewClient: WebViewClient() {
    private val login_url = "https://educonnect.education.gouv.fr/idp/profile/SAML2/Redirect/SSO?execution=e1s1"

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        if(request == null) return false
        /*Log.i("$tag/req", "${if(request.isRedirect) "(redirect)" else ""} ${request.method.uppercase()} ${request.url}")
        Log.i("$tag/req/headers", "${request.requestHeaders}")*/
        return false
    }

    override fun onLoadResource(view: WebView?, url: String?) {
        if(url != null && url.matches(Regex("/(https?:\\/\\/([a-zA-Z0-9.]+)([a-zA-Z0-9-_\\/]+)((\\.(css|woff2|png|svg|js))))/g")))
            Log.i("$tag/res", "Loading rescource : $url")
        super.onLoadResource(view, url)
    }

    override fun onReceivedHttpAuthRequest(
        view: WebView?,
        handler: HttpAuthHandler?,
        host: String?,
        realm: String?
    ) {
        Log.i("$tag/req/auth", "Auth request : $host, realm : $realm")
        super.onReceivedHttpAuthRequest(view, handler, host, realm)
    }

    override fun onReceivedLoginRequest(
        view: WebView?,
        realm: String?,
        account: String?,
        args: String?
    ) {
        Log.i("$tag/req/login", "Login request : $realm, account : $account, args : $args")
        super.onReceivedLoginRequest(view, realm, account, args)
    }

    override fun shouldInterceptRequest(
        view: WebView?,
        request: WebResourceRequest?
    ): WebResourceResponse? {
        if(request == null) return null
        if(request.url.toString().matches(Regex(pattern = "(https?:\\/\\/([a-zA-Z0-9-_.]+)([a-zA-Z0-9-_\\/]+)(\\.(css|woff2|png|svg|js))\\?*\\S*)", options = setOf(RegexOption.IGNORE_CASE))))
            {/*Log.i("$tag/req/passed", "${request.url}");*/ return null;}
        Log.i("$tag/req", "${if(request.isRedirect) "(redirect) " else ""}${request.method.uppercase()} ${request.url}")
        Log.i("$tag/req/headers", "\t${request.requestHeaders}")
        return super.shouldInterceptRequest(view, request)
    }
}