package yet.net

/**
 * @see java.net.Inet4Address
 * Created by entaoyang@163.com on 2016-07-31.
 */

object IP {
	private val ipReg = "[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}".toRegex()

	fun isIP4(ip: String?): Boolean {
		return ip?.matches(ipReg) ?: false
	}

	//本机回路127.x.x.x
	fun isLoopback(ip: String?): Boolean {
		if (ip != null) {
			if (isIP4(ip)) {
				return ip.startsWith("127.")
			}
		}
		return false
	}

	//内网192.168.x.x,  10.x.x.x
	fun isLAN(ip: String?): Boolean {
		if (ip != null) {
			if (isIP4(ip)) {
				if (ip.startsWith("192.168.")) {
					return true
				}
				if (ip.startsWith("10.")) {
					return true
				}
				//172.16.0.0-172.31.255.255
				if (ip.startsWith("172.") && ip[6] == '.') {
					val s = ip.substring(4, 6)
					val n = s.toInt()
					return n in 16..31
				}
			}
		}
		return false
	}
}