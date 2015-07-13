import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public abstract class NothingSelectedSpinnerAdapter<T> extends BaseAdapter {

	private static final int DUMMY_DROPDOWN_VIEW_TYPE_TAG = 1;
	private final Spinner mSpinner;
	private T mNothingSelectedDataItem;

	public NothingSelectedSpinnerAdapter(Spinner spinner) {
		mSpinner = spinner;
	}

	/**
	 * @param position position == -1 equals nothing selected
	 */
	public void setSelection(int position) {
		mSpinner.setSelection(position + 1);
	}

	public void setNothingSelectedDataItem(T nothingSelectedDataItem) {
		mNothingSelectedDataItem = nothingSelectedDataItem;
	}

	public T getNothingSelectedDataItem() {
		return mNothingSelectedDataItem;
	}

	public int getSelectedDataItemPosition() {
		final int pos = mSpinner.getSelectedItemPosition();
		return pos == 0 || pos == AdapterView.INVALID_POSITION ? AdapterView.INVALID_POSITION : pos - 1;
	}

	public T getSelectedDataItem() {
		final int pos = mSpinner.getSelectedItemPosition();
		return pos == 0 || pos == AdapterView.INVALID_POSITION ? null : getDataItem(pos - 1);
	}

	@Override
	public final View getView(int position, View convertView, ViewGroup parent) {
		return getViewStaff(position == 0 ? AdapterView.INVALID_POSITION : position - 1, convertView, parent);
	}

	@Override
	public final View getDropDownView(int position, View convertView, ViewGroup parent) {
		if (position == 0) {
			convertView = new View(mSpinner.getContext());
			convertView.setTag(DUMMY_DROPDOWN_VIEW_TYPE_TAG);
			return convertView;
		} else {
			return getDropDownViewStaff(position - 1, convertView, parent);
		}
	}

	private View getViewStaff(int position, View convertView, ViewGroup parent) {
		return getCustomView(android.R.layout.simple_spinner_item, position, convertView, parent);
	}

	private View getDropDownViewStaff(int position, View convertView, ViewGroup parent) {
		return getCustomView(android.R.layout.simple_spinner_dropdown_item, position, convertView, parent);
	}

	private View getCustomView(int resId, int position, View convertView, ViewGroup parent) {
		if (convertView == null || convertView.getTag() != null && (Integer)convertView.getTag() == DUMMY_DROPDOWN_VIEW_TYPE_TAG) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(resId, parent, false);
		}
		final TextView textView = (TextView) convertView;
		final String text = position == AdapterView.INVALID_POSITION ? mNothingSelectedDataItem == null ? getDataItemText(0) :
				getNothingSelectedText(mNothingSelectedDataItem) : getDataItemText(position);
		textView.setText(text);
		return textView;
	}

	/**
	 * @param nothingSelectedDataItem not null
	 */
	protected abstract String getNothingSelectedText(T nothingSelectedDataItem);

	protected abstract String getDataItemText(int position);

	protected abstract int getDataItemCount();

	protected abstract T getDataItem(int position);

	@Override
	public final int getCount() {
		final int count = getDataItemCount();
		return count == 0 ? 0 : count + 1;
	}

	@Override
	public final Object getItem(int position) {
		return null;
	}

	@Override
	public final long getItemId(int position) {
		return position;
	}

	@Override
	public final boolean hasStableIds() {
		return false;
	}

	@Override
	public final boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return position != 0; // Don't allow the 'nothing selected' item to be picked
	}
