<p>This Java Webapp implements the most common user management flows within a webapp.</p>
    <ul>
        <li>Sign up with Email verificiation</li>
        <li>Login with hashed password check</li>
        <li>User Recovery via Email one time link</li>
        <li>Email address change with verification</li>
        <li>Password change</li>
        <li>User deletion</li>
    </ul>
    <p>It uses a http only secure cookie with strict same site attribute and invalidates the cookie on logout. </p>
    <p>To run it you must edit the config.properties in the resources folder(also in the test folder for the tests).</p>
    <ul>
        <li>pathToCert: path to X.509 certificate for public key (as pem)</li>
        <li>pathToPrivKey: path to corresponding private key pem file</li>
        <li>fromEmail: Email sender for the various email flows</li>
        <li>smtpUserName: for the smtp host login</li>
        <li>smtpPassword: for the smtp host login</li>
        <li>smtpHost: for the smtp host login</li>
        <li>smtpPort: for the smtp host login</li>
        <li>or set isMailingEnabled to false</li>
    </ul>
    <p>Udpate will com soon</p>
