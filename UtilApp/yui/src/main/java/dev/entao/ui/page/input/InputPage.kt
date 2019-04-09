package dev.entao.ui.page.input

import android.content.Context
import android.view.ViewGroup
import android.widget.*
import dev.entao.appbase.ex.Colors
import dev.entao.base.*
import dev.entao.log.logd
import dev.entao.sql.Required
import dev.entao.sql.hasAnnotation
import dev.entao.sql.selectOptionsStatic
import dev.entao.theme.ViewSize
import dev.entao.ui.ext.*
import dev.entao.ui.list.views.TextDetailView
import dev.entao.ui.list.views.textDetail
import dev.entao.ui.page.TitlePage
import dev.entao.ui.res.Res
import dev.entao.ui.util.TimeDown
import dev.entao.ui.viewcreator.*
import dev.entao.util.Task
import dev.entao.util.ToastUtil
import yet.ext.defaultValueOfProperty
import yet.ext.strToV
import java.util.regex.Pattern
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0
import kotlin.reflect.KProperty1

/**
 * Created by entaoyang@163.com on 2016-10-20.
 */

abstract class InputPage : TitlePage() {

	lateinit var inputLayout: LinearLayout

	var INPUT_HEIGHT = ViewSize.EditHeight
	var inputMarginTop = 10
	var buttonMarginTop = 30

	private val editList = ArrayList<Triple<String, EditText, KProperty<*>?>>()
	private val checkMap = HashMap<String, CheckBox>()
	private val dateMap = HashMap<String, TextDetailView>()
	private val dateFormatMap = HashMap<String, String>().withDefault { MyDate.FORMAT_DATE }
	private val selectMap = HashMap<String, TextDetailView>()
	private val validMap = LinkedHashMap<String, InputValid>()
	private var codeEdit: EditText? = null
	private var codeButton: Button? = null
	private var timeDownKey: String = System.currentTimeMillis().toString()
	private var codeClickTime: Long = 0

	init {
		enableContentScroll = true
	}

	fun fromEdit(model: Any, ps: List<KMutableProperty1<*, *>>) {
		for (p in ps) {
			val s = getText(p.nameProp)
			val v: Any = strToV(s, p) ?: defaultValueOfProperty(p)
			p.setValue(model, v)
		}
	}


	fun edit(block: InputOption.() -> Unit = {}): EditText {
		val io = InputOption()
		io.height = ViewSize.EditHeight
		io.block()
		if (io.inputValid.label.isEmpty()) {
			io.inputValid.label(io.hint)
		}
		val ed = inputLayout.editX(LParam.WidthFill.height(io.height).margins(io.marginLeft, io.marginTop, io.marginRight, io.marginBottom)) {
			padding(5, 2, 5, 2)
			var hintStr = io.hint
			if (io.inputValid.require) {
				if (!hintStr.endsWith('*')) {
					hintStr += "*"
				}
			}
			hint = hintStr
			setText(io.value)
		}
		editList.add(Triple(io.key, ed, io.prop))
		validMap[io.key] = io.inputValid
		return ed
	}

	fun phone(block: InputOption.() -> Unit = {}): EditText {
		val ed = edit {
			hint = "请输入手机号"
			valid {
				phone11()
			}
			this.block()
		}
		ed.inputTypePhone()
		return ed
	}

	fun email(block: InputOption.() -> Unit = {}): EditText {
		val ed = edit {
			hint = "请输入邮箱"
			valid {
				email()
			}
			this.block()
		}
		ed.inputTypeEmail()
		return ed
	}

	fun number(block: InputOption.() -> Unit = {}): EditText {
		val ed = edit {
			valid {
				numbers()
			}
			this.block()
		}
		ed.inputTypeNumber()
		return ed
	}

	fun password(block: InputOption.() -> Unit = {}): EditText {
		val ed = edit {
			hint = "请输入密码"
			valid {
				notEmpty()
			}
			this.block()
		}
		ed.inputTypePassword()
		return ed
	}

	fun passwordAgain(block: InputOption.() -> Unit = {}): EditText {
		val ed = edit {
			hint = "请再次输入密码"
			valid {
				notEmpty()
			}
			this.block()
		}
		ed.inputTypePassword()
		return ed
	}

	fun passwordNumber(block: InputOption.() -> Unit = {}): EditText {
		val ed = edit {
			hint = "请输入数字密码"
			valid {
				notEmpty()
				numbers()
			}
			this.block()
		}
		ed.inputTypePasswordNumber()
		return ed
	}

	fun passwordNumberAgain(block: InputOption.() -> Unit = {}): EditText {
		val ed = edit {
			hint = "请再次输入数字密码"
			valid {
				notEmpty()
				numbers()
			}
			this.block()
		}
		ed.inputTypePasswordNumber()
		return ed
	}

	fun checkbox(block: InputOption.() -> Unit = {}): CheckBox {
		val io = InputOption()
		io.height = ViewGroup.LayoutParams.WRAP_CONTENT
		io.block()
		if (io.inputValid.label.isEmpty()) {
			io.inputValid.label(io.hint)
		}
		io.inputValid.label(io.hint)
		val cb = inputLayout.checkBox(LParam.WidthFill.height(io.height).margins(io.marginLeft, io.marginTop, io.marginRight, io.marginBottom)) {
			padding(10, 5, 5, 5)
			this.hint = io.hint
			if (io.value.isEmpty()) {
				this.text = io.hint
			} else {
				this.text = io.value
			}
		}
		checkMap[io.key] = cb
		validMap[io.key] = io.inputValid
		return cb
	}

	fun static(label: String, marginTop: Int = inputMarginTop): TextView {
		return inputLayout.textView(lParam().widthFill().height(INPUT_HEIGHT).margins(0, marginTop, 0, 0)) {
			text = label
		}
	}

	fun label(label: String, marginTop: Int = inputMarginTop): TextView {
		return inputLayout.textView(lParam().widthFill().heightWrap().margins(0, marginTop, 0, 0)) {
			text = label
		}
	}

	fun image(label: String, imgHeight: Int = INPUT_HEIGHT, marginTop: Int = inputMarginTop): TextView {
		return inputLayout.textView(lParam().widthFill().height(imgHeight).margins(0, marginTop, 0, 0)) {
			text = label
			rightImage(Res.imageMiss, imgHeight)
		}
	}

	fun button(key: String, title: String, marginTop: Int = buttonMarginTop): Button {
		val b = inputLayout.button(LParam.WidthFill.HeightButton.margins(0, marginTop, 0, 0)) {
			setOnClickListener { _onButtonClick(key) }
			text = title
		}
		return b
	}

	fun buttonSafe(key: String, title: String, marginTop: Int = buttonMarginTop): Button {
		return button(key, title, marginTop).styleGreen()
	}

	fun buttonRed(key: String, title: String, marginTop: Int = buttonMarginTop): Button {
		return button(key, title, marginTop).styleRed()
	}

	fun buttonWhite(key: String, title: String, marginTop: Int = buttonMarginTop): Button {
		return button(key, title, marginTop).styleWhite()
	}

	fun textDetail(title: String, marginTop: Int = inputMarginTop): TextDetailView {
		return inputLayout.textDetail(LParam.WidthFill.height(INPUT_HEIGHT).margins(0, marginTop, 0, 0)) {
			padding(0, 0, 0, 0)
			detailView.textSizeB().gravityCenter().padding(10, 5, 10, 5)
			detailView.miniWidthDp(100)
			detailView.miniHeightDp(ViewSize.ButtonHeightSmall - 10)
			textView.text = title
		}
	}

	fun date(key: String, title: String, format: String = MyDate.FORMAT_DATE, marginTop: Int = inputMarginTop): TextDetailView {
		val v = textDetail(title, marginTop)
		v.detailView.backStrike(Colors.TRANS, 3, 1, Colors.LineGray)
		dateMap[key] = v
		dateFormatMap[key] = format
		setDate(key, 0L)
		v.onClick {
			pickDate(getDate(key)) {
				setDate(key, it)
			}
		}
		return v
	}

	fun selectMap(p: Prop1, optMap: Map<Any, String>): TextDetailView {
		val v = textDetail(p.labelProp_)
		if (p.hasAnnotation<Required>()) {
			v.textView.text = p.labelProp_ + "*"
		}
		v.detailView.backStrike(Colors.TRANS, 3, 1, Colors.LineGray)
		selectMap[p.nameProp] = v
		v.onClick {
			selectItem(optMap.keys, { optMap[it] ?: "" }) {
				v.tag = it
				v.setDetail(optMap[it] ?: "")
				logd("SelectItem: tag = ", v.tag, " val=", optMap[it])
			}
		}
		return v
	}

	fun select(p: Prop1): TextDetailView {
		return selectMap(p, p.selectOptionsStatic.mapKeys { it.key as Any })
	}

	fun select(p: KProperty1<*, *>, items: Collection<Any>, displayProp: KProperty1<*, *>, idProp: KProperty1<*, *>): TextDetailView {
		val v = textDetail(p.labelProp_)
		if (p.hasAnnotation<Required>()) {
			v.textView.text = p.labelProp_ + "*"
		}
		v.detailView.backStrike(Colors.TRANS, 3, 1, Colors.LineGray)
		selectMap[p.nameProp] = v
		v.onClick {
			selectItem(items, displayProp) {
				v.tag = idProp.getValue(it)
				v.setDetail(displayProp.getValue(it)?.toString() ?: "")
			}
		}
		return v
	}

	fun select(key: String, title: String, value: String, items: List<String>): TextDetailView {
		val v = textDetail(title)
		v.detailView.backStrike(Colors.TRANS, 3, 1, Colors.LineGray)
		selectMap[key] = v
		v.setDetail(value)
		v.tag = value
		v.onClick {
			selectString(items) { item ->
				v.tag = item
				v.setDetail(item)
			}

		}
		return v
	}

	fun selectPair(p: KProperty<*>, items: List<Pair<String, String>>): TextDetailView {
		val v = if (p is KProperty0<*>) p.getValue()?.toString() ?: "" else ""
		val lb = if (p.hasAnnotation<Required>()) {
			p.labelProp_ + "*"
		} else {
			p.labelProp_
		}
		return selectPair(p.nameProp, lb, v, items)
	}

	fun selectPair(key: String, title: String, value: String, items: List<Pair<String, String>>): TextDetailView {
		val v = textDetail(title)
		v.detailView.backStrike(Colors.TRANS, 3, 1, Colors.LineGray)
		selectMap[key] = v
		val item = items.find { it.first == value }
		v.setDetail(item?.second)
		v.tag = item?.first
		v.onClick {
			selectStringN(items.map { it.second }) {
				v.tag = items[it].first
				v.setDetail(items[it].second)
			}

		}
		return v
	}

	fun valid(): Boolean {
		for ((k, v) in validMap.entries) {
			val ed = editList.find { it.first == k }!!.second
			val info = v.checkAll(ed)
			if (info != null) {
				alert(info)
				return false
			}

		}
		return true
	}


	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		inputLayout = contentView.linearVer(lParam().widthFill().heightWrap()) {
			padding(30, 25, 30, 20)
		}
	}

	override fun onContentCreated() {
		super.onContentCreated()
		val sz = editList.size
		editList.forEachIndexed { n, p ->
			if (n < sz - 1) {
				p.second.imeNext {
					editList[n + 1].second.requestFocus()
				}
			} else {
				p.second.imeDone {
					hideInputMethod()
				}
			}
		}
	}


	//中国的11位手机号码格式, 连续11位数字,1开头
	fun isPhoneFormatCN11(s: String?): Boolean {
		val ss = s ?: return false
		val regex = "1[0-9]{10}"
		val p = Pattern.compile(regex)
		val m = p.matcher(ss)
		return m.find()
	}

	fun isCheck(key: String): Boolean {
		return checkMap[key]?.isChecked ?: false
	}

	fun setCheck(key: String, check: Boolean) {
		checkMap[key]?.isChecked = check
	}

	val code: String
		get() {
			return codeEdit?.text?.toString() ?: ""
		}

	fun getText(key: String): String {
		val s = editList.find { it.first == key }?.second?.text?.toString() ?: ""
		val v = validMap[key]
		if (v != null && v.trimText) {
			return s.trim()
		}
		return s
	}

	fun setText(key: String, text: String) {
		editList.find { it.first == key }?.second?.setText(text)
	}

	fun disableEdit(key: String) {
		editList.find { it.first == key }?.second?.isEnabled = false
	}

	fun getDate(key: String): Long {
		val s = dateMap[key]!!.detailView.text!!.toString()
		if (s.isNotEmpty()) {
			return MyDate.parse(dateFormatMap[key] ?: MyDate.FORMAT_DATE, s)!!.time
		}
		return 0
	}

	fun setDate(key: String, date: Long) {
		dateMap[key]?.setDetail(MyDate(date).format(dateFormatMap[key] ?: MyDate.FORMAT_DATE))
	}


	fun getSelectValue(key: String): String {
		return selectMap[key]?.tag as? String ?: ""
	}

	fun startTimeDown(seconds: Int) {
		Task.fore {
			TimeDown.startClick(timeDownKey, seconds, codeButton!!)
		}
	}


	fun addVerifyCode(phoneEditKey: String, marginTop: Int = inputMarginTop) {

		inputLayout.linearHor(LParam.WidthFill.HeightWrap.margins(0, marginTop, 0, 0)) {
			codeEdit = edit(LParam.FlexHor.heightEdit()) {
				hint = "输入验证码"
				inputTypeNumber()
			}
			codeButton = button(LParam.WidthWrap.HeightButtonSmall.margins(3, 0, 0, 0)) {
				text = "获取验证码"
				styleWhite(6)
			}
		}
		TimeDown.updateView(this.timeDownKey, codeButton!!)
		codeButton?.onClick {
			codeClickTime = System.currentTimeMillis()
			val phone = getText(phoneEditKey)
			if (!isPhoneFormatCN11(phone)) {
				ToastUtil.show("手机号格式错误")
			} else {
				startTimeDown(60)
				onCodeButtonClick(phone)
			}
		}
	}

	open fun onCodeButtonClick(phone: String) {

	}


	private fun _onButtonClick(key: String) {
		hideInputMethod()
		onButtonClick(key)
	}

	abstract fun onButtonClick(key: String)
}