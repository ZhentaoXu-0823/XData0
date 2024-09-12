/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package serial.utils;

import android.content.Context;

public abstract class BaseControl {

    public OnCompletionListener mOnCompletionListener = null;

    public interface OnCompletionListener {
        public void onCompletion(BaseControl control, boolean result);
    }

    public void setOnCompletionListener(OnCompletionListener listener) {
        mOnCompletionListener = listener;
    }

    BaseControl(Context context) {
    }

    public abstract boolean isNeedTest(String name);

    public abstract String getName();

    public abstract void startTest();

    public abstract void stopTest(boolean result);

    public void release() {
        if (mOnCompletionListener != null) {
            mOnCompletionListener = null;
        }
    }
}
