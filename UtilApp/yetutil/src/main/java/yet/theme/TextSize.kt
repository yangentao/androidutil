package yet.theme

/**
 * Created by entaoyang@163.com on 16/7/21.
 */
object TextSize {
	var Large = 20// sp
	var Title = 18// sp
	var Big = 18// sp
	var Normal = 16// sp
	var Small = 14// sp
	var Tiny = 12// sp

	fun addTextSize(n: Int) {
		Large += n
		Title += n
		Big += n
		Normal += n
		Small += n
		Tiny += n
	}
}
