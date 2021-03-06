package org.jdatepicker.constraints;

import org.jdatepicker.DateModel;

import java.util.Calendar;
import java.util.Date;

public class RangeConstraint implements DateSelectionConstraint {

	private final Calendar after;
	private final Calendar before;

	public RangeConstraint(Calendar after, Calendar before) {
		this.after = after;
		this.before = before;

		// remove hours / minutes / seconds from dates
		cleanTime();
	}

	public RangeConstraint(Date after, Date before) {
		Calendar _after = Calendar.getInstance();
		Calendar _before = Calendar.getInstance();

		_after.setTime(after);
		_before.setTime(before);

		this.after = _after;
		this.before = _before;

		// remove hours / minutes / seconds from dates
		cleanTime();
	}

	private void cleanTime() {
		if (after != null) {
			after.set(Calendar.HOUR_OF_DAY, 0);
			after.set(Calendar.MINUTE, 0);
			after.set(Calendar.SECOND, 0);
			after.set(Calendar.MILLISECOND, 0);
		}

		if (before != null) {
			before.set(Calendar.HOUR_OF_DAY, 23);
			before.set(Calendar.MINUTE, 59);
			before.set(Calendar.SECOND, 59);
			before.set(Calendar.MILLISECOND, 999);
		}
	}

	public boolean isValidSelection(DateModel model) {
		boolean result = true;

		if (model.isSelected() && after != null) {
            Calendar value = Calendar.getInstance();
            value.set(model.getYear(), model.getMonth(), model.getDay());
            value.set(Calendar.HOUR, 0);
            value.set(Calendar.MINUTE, 0);
            value.set(Calendar.SECOND, 0);
            value.set(Calendar.MILLISECOND, 0);
			result &= value.after(after);
		}
		if (model.isSelected() && before != null) {
            Calendar value = Calendar.getInstance();
            value.set(model.getYear(), model.getMonth(), model.getDay());
            value.set(Calendar.HOUR, 0);
            value.set(Calendar.MINUTE, 0);
            value.set(Calendar.SECOND, 0);
            value.set(Calendar.MILLISECOND, 0);
			result &= value.before(before);
		}

		return result;
	}

	@Override
	// Generated with eclipse depending on: after, before
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((after == null) ? 0 : after.hashCode());
		result = prime * result + ((before == null) ? 0 : before.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
            return true;
        }
		if (obj == null) {
            return false;
        }
		if (getClass() != obj.getClass()) {
            return false;
        }

		RangeConstraint other = (RangeConstraint) obj;
		if (after == null) {
			if (other.after != null) {
                return false;
            }
		}
        else if (!after.equals(other.after)) {
            return false;
        }
		if (before == null) {
			if (other.before != null) {
                return false;
            }
		}
        else if (!before.equals(other.before)) {
            return false;
        }
		return true;
	}

}
