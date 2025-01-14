package me.anon.lib

import android.app.Activity
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.view.View

abstract class SnackBarListener
{
	abstract fun onSnackBarStarted(o: Any)
	abstract fun onSnackBarFinished(o: Any)
	abstract fun onSnackBarAction(o: View)
}

/**
 * Snackbar helper class
 */
class SnackBar
{
	companion object
	{
		@JvmStatic
		public fun show(context: Activity, @StringRes messageRes: Int, @StringRes actionTextRes: Int = -1,
			listener: SnackBarListener?
		)
		{
			show(context, context.getString(messageRes),
				if (actionTextRes != -1) context.getString(actionTextRes) else "",
				Snackbar.LENGTH_LONG,
				listener
			)
		}

		@JvmStatic
		public fun show(context: Activity, message: String, listener: SnackBarListener?)
		{
			show(context, message, "", Snackbar.LENGTH_LONG, listener)
		}

		@JvmStatic
		public fun show(context: Activity, message: String, length: Int, listener: SnackBarListener?)
		{
			show(context, message, "", length, listener)
		}

		@JvmStatic
		public fun show(context: Activity, message: String, actionText: String = "",
			listener: SnackBarListener?
		)
		{
			show(context, message, actionText, Snackbar.LENGTH_LONG, listener)
		}

		@JvmStatic
		public fun show(context: Activity, message: String, actionText: String = "", length: Int = Snackbar.LENGTH_LONG,
			listener: SnackBarListener?
		)
		{
			SnackBar().show(context, message, actionText, Snackbar.LENGTH_LONG, {
				listener?.onSnackBarStarted(0)
			}, {
				listener?.onSnackBarFinished(0)
			}, { v ->
				listener?.onSnackBarAction(v)
			})
		}
	}

	public fun show(context: Activity, @StringRes messageRes: Int)
	{
		show(context, messageRes)
	}

	public fun show(context: Activity, @StringRes messageRes: Int, @StringRes actionTextRes: Int = -1,
		start: () -> kotlin.Unit = {},
		end: () -> kotlin.Unit = {},
		action: (View) -> kotlin.Unit = {}
	)
	{
		show(context, context.getString(messageRes),
			if (actionTextRes != -1) context.getString(actionTextRes) else "",
			Snackbar.LENGTH_LONG,
			start, end, action
		)
	}

	public fun show(context: Activity, message: String, actionText: String = "", length: Int = Snackbar.LENGTH_LONG,
		start: () -> kotlin.Unit = {},
		end: () -> kotlin.Unit = {},
		action: (View) -> kotlin.Unit = {}
	)
	{
		val snackbar = Snackbar.make(context.findViewById(android.R.id.content), message, length)
		var actionText = actionText
		var action = action

		if (length == Snackbar.LENGTH_INDEFINITE)
		{
			actionText = "Dismiss"
			action = {
				snackbar.dismiss()
			}
		}

		if (actionText.isNotEmpty())
		{
			snackbar.setAction(actionText) { v ->
				action.invoke(v)
			}
		}

		snackbar.setCallback(object : Snackbar.Callback()
		{
			override fun onShown(sb: Snackbar?)
			{
				start.invoke()
			}

			override fun onDismissed(transientBottomBar: Snackbar?, event: Int)
			{
				end.invoke()
			}
		})


		snackbar.show()
	}
}
