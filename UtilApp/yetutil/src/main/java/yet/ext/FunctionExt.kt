package yet.ext

import kotlin.jvm.internal.CallableReference
import kotlin.jvm.internal.FunctionReference
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.valueParameters

/**
 * Created by entaoyang@163.com on 2017/4/28.
 */

/*
* class A(){
*       fun fa(){}
* }
*
* class B: A(){
*
* }
*
* A::fa.ownerClass => A::class
* A()::fa.ownerClass => A::class
* B::fa.ownerClass => B::class
* B()::fa.ownerClass => B::class
 */
val KFunction<*>.ownerClass: KClass<*>? get() = (this as? FunctionReference)?.owner as? KClass<*>
val KFunction<*>.boundClass: KClass<*>? get() {
	val inst = this.boundReceiver
	if (inst != null) {
		return inst::class
	}
	return null
}

val KFunction<*>.boundReceiver: Any? get() {
	if (this is FunctionReference) {
		if (this.boundReceiver != CallableReference.NO_RECEIVER) {
			return this.boundReceiver
		}
	}
	return null
}

val KFunction<*>.paramName1: String? get() {
	return this.valueParameters.firstOrNull()?.name
}
