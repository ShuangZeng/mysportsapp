package com.demo.mysportsapp.common.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView

import android.view.inputmethod.EditorInfo

import android.view.WindowManager

import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.demo.mysportsapp.R


class TextPromptDialog : DialogFragment() {
    lateinit var textEditTextView: EditText
    var okTextView: TextView? = null
    var changeListener: TextPromptChangeListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(getActivity())
        val view: View =
            activity?.layoutInflater!!.inflate(R.layout.dialog_text_prompt, null)
        textEditTextView = view.findViewById(R.id.edtxt_enter_text)
        okTextView = view.findViewById(R.id.txt_ok)
        builder.setView(view)
        initialize()
        val dialog: Dialog = builder.create()
        val window: Window? = dialog.getWindow()
        if (window != null) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
        textEditTextView.requestFocus()
        textEditTextView.setOnEditorActionListener { v, actionId, event ->
            if (actionId === EditorInfo.IME_ACTION_DONE) {
                validateTextAndReturn()
                return@setOnEditorActionListener true
            }
            false
        }
        return dialog
    }

    fun setListener(changeListener: TextPromptChangeListener?) {
        this.changeListener = changeListener
    }

    private fun initialize() {
        okTextView!!.setOnClickListener { view -> validateTextAndReturn() }
    }

    private fun validateTextAndReturn() {
        if (textEditTextView.getText() != null && textEditTextView.getText().isNotEmpty()) {
            if (changeListener != null) changeListener!!.onTextChanged(textEditTextView.getText().toString())
            dismiss()
        } else {
            Toast.makeText(activity,"Please enter some text",Toast.LENGTH_SHORT).show()
        }
    }

    interface TextPromptChangeListener {
        fun onTextChanged(text: String?)
    }
}
class TitleDetailView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs)
{
    private var detailText: String?=""
    private var titleText: String?=""
    private lateinit var titleTextView:TextView
    private lateinit var detailTextView:TextView

    init {
        attrs?.let {
            val a = getContext().obtainStyledAttributes(it, R.styleable.TitleDetailView)
            titleText = a.getString(R.styleable.TitleDetailView_title)
            detailText = a.getString(R.styleable.TitleDetailView_detail)
            val v = inflate(context, R.layout.title_detail_layout, this)
            orientation = LinearLayout.VERTICAL
            val padding: Int = resources.getDimensionPixelSize(R.dimen.titleDetailPadding)
            setPadding(padding,padding,padding,padding)
            titleTextView=v.findViewById(R.id.titleTextView)
            detailTextView=v.findViewById(R.id.detailTextView)
            setTitle(titleText)
            setDetail(detailText)

        }
    }

    fun setTitle(text:String?)
    {
        titleTextView.text=text
    }

    fun setDetail(text:String?)
    {
        detailTextView.text=text
    }

    fun setTitleDetail(titleDetailDTO:TitleDetailViewDTO)
    {
        setTitle(titleDetailDTO.title)
        setDetail(titleDetailDTO.detail)
    }

}

data class TitleDetailViewDTO(val title:String,val detail:String)
