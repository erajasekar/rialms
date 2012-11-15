email {
  from = 'do.not.reply@localhost'
  register {
    body = '''\
Dear $name,<br/>
<br/>
Welcome to Rialms!. Thank you for signing up.
<br/>
Please click <a href="$url">confirm</a> to activate your account.
'''
    subject = 'Confirm your registration with Rialms'
  }

  forgotPassword {
    body = '''\
Hi ,<br/>
<br/>
A request has been made to reset password for your Rialms account.<br/>
<br/>
To choose a new password, click on the link </br>
<a href="$url">$url</a>
'''
    subject = 'Rialms Password Recovery'
  }
}

