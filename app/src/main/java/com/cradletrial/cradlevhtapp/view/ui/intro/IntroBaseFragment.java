package com.cradletrial.cradlevhtapp.view.ui.intro;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.cradletrial.cradlevhtapp.model.Reading;
import com.cradletrial.cradlevhtapp.model.ReadingManager;

/**
 * Base class for other ReadingFragments
 * - Observer
 * - Shared access to Reading object

 * Activities that contain fragments derived from this class  must implement the
 * {@link MyIntroFragmentInteractionListener} interface to handle interaction events.
 * Use the newInstance() factory method to create an instance of derived fragment.
 */
abstract public class IntroBaseFragment extends Fragment {
    protected String TAG = IntroBaseFragment.class.getName();

    protected MyIntroFragmentInteractionListener activityCallbackListener;
    protected Reading currentReading;
    protected ReadingManager readingManager;

    protected long MASK_USER_HAS_SELECTED_THE_NO_SYMPTOM = 0x0001;
    protected long MASK_USER_HAS_CHANGED_FOLLOW_UP = 0x0002;
    protected long MASK_USER_HAS_CHANGED_RECHECK_OPTION = 0x0004;

    public IntroBaseFragment() {
        // Empty
    }

    /*
        Keyboard support
     */
    public void hideKeyboard() {
        // SOURCE: https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
        Context context = getContext();
        View view = this.getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /*
        Observer
     */
    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "TRACE -- onAttach(): hash" + toString());
        super.onAttach(context);

        // ensure callback functions are implemented.
        if ( !(context instanceof MyIntroFragmentInteractionListener)) {
            throw new RuntimeException(context.toString()
                    + " must implement MyFragmentIntroInteractionListener");
        }
        activityCallbackListener = (MyIntroFragmentInteractionListener) context;
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "TRACE -- onDetatch(): hash" + toString());
        super.onDetach();
        activityCallbackListener = null;
    }

    /**
     * Called by ReadingActivity when fragment is just being displayed (navigated to)
     */
    abstract public void onMyBeingDisplayed();
    /**
     * Called by ReadingActivity after fragment has been navigated away from (hidden)
     */
    abstract public boolean onMyBeingHidden();
}
