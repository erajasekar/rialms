package com.rialms.auth

class User {

	transient springSecurityService

	String email
    String displayName
	String password
	boolean enabled
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired
    Date dateCreated
    Date lastLogggedIn
    int loginCount;

    static hasMany = [openIds: OpenID]

	static constraints = {
		email blank: false, unique: true
		password blank: false
        lastLogggedIn (nullable:true);
	}

	static mapping = {
		password column: '`password`'
	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role } as Set
	}

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService.encodePassword(password)
	}
}
