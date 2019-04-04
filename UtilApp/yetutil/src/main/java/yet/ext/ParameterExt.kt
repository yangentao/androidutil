package yet.ext

import yet.anno.Name
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation

/**
 * Created by entaoyang@163.com on 2017-04-16.
 */


val KParameter.customName: String get() {
	return this.findAnnotation<Name>()?.value ?: this.name ?: throw IllegalStateException("参数没有名字")
}