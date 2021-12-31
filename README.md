<h1> Java Web app providing Registration, Login and Data Change flows</h1>


<ul>
    <li><h5>This includes:</h5></li>
        <li>Sign up with Email verificiation</li>
        <li>Login</li>
        <li>User/Password Recovery via Email one time link</li>
        <li>Email address change with verification</li>
        <li>Password change</li>
        <li>User deletion</li>
</ul>

<h2> Frontend is build with </h2>
     <ul>
        <li>html5</li>
        <li>Javascript</li>
        <li>JQuery</li>
        <li>Bootstrap</li>
        <li>User deletion</li>
    </ul>
<h2> Backend (Java) is build with </h2>
     <ul>
        <li>Java 8</li>
        <li>Spring Boot</li>
        <li>Spring Data JPA</li>
        <li>Spring Mail</li>
        <li>Spring REST</li>
        <li>Freemarker for Emails</li>
        <li>HSQL DB</li>
        <li>JUnit5</li>
</ul>
<h2> Security </h2>
     <ul>
        <li>provides https only connections (with redirect from http port)</li>
        <li>It uses a http only secure cookie with lax same site attribute and invalidates the cookie on logout.</li>
        <li>The cookie contains an JWT ID Token which is signed and verified on interaction</li>
        <li>Password is hashed with BCrypt</li>
</ul>


<h2> Get it running </h2>    
    <h5> Secrets</h5>
    <ul>
        <li>config.properties (in test/main resources) pathToCert: path to X.509 certificate for public key (as pem)</li>
        <li>config.properties (in test/main resources) pathToPrivKey: path to corresponding private key pem file</li>
        <li>application.properties (in main resources) configure ssl keystore for https connection (server.ssl.key-store *) </li>
        <li>or set isMailingEnabled to false</li>
    </ul>
    <h5> Mail</h5>
    <ul>
        <li>application.properties (in main resources) configure spring.mail.* for sending emails</li>
    </ul>
    <h5> Database</h5>
    <ul>
        <li>Units tests are running with HSQL</li>
        <li>local start would in the current configuration also run with HSQL (that means on app server restart data is lost)</li>
        <li>To change update application.properties (in main resources) spring.datasource.*/ spring.jpa* </li>
    </ul>
    <h5> Other configs</h5>
    <ul>
        <li>Have a closer look at config.properties (in test/main resources)</li>
        <li>E.g. switch off/on email sending by isMailingEnabled</li>
        <li>Switch off/on if user needs to click activation link to be active </li>
        <li>http port for https redirect (not nice but works) </li>
        <li>Id Token configs (TTL) </li>
        <li>base Urls for links in emails</li>
        <li>...</li>
    </ul>