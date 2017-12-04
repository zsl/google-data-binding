/*
 * Copyright (C) 2017 The Android Open Source Project
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

package android.databinding;


import android.view.View;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A data binding mapper that merges other mappers.
 */
public class MergedDataBinderMapper extends DataBinderMapper {
    private List<DataBinderMapper> mMappers = new CopyOnWriteArrayList<>();

    protected void addMapper(DataBinderMapper mapper) {
        mMappers.add(mapper);
    }

    @Override
    public ViewDataBinding getDataBinder(DataBindingComponent bindingComponent, View view,
            int layoutId) {
        for(DataBinderMapper mapper : mMappers) {
            ViewDataBinding result = mapper.getDataBinder(bindingComponent, view, layoutId);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    @Override
    public ViewDataBinding getDataBinder(DataBindingComponent bindingComponent, View[] view,
            int layoutId) {
        for(DataBinderMapper mapper : mMappers) {
            ViewDataBinding result = mapper.getDataBinder(bindingComponent, view, layoutId);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    @Override
    public int getLayoutId(String tag) {
        for(DataBinderMapper mapper : mMappers) {
            int result = mapper.getLayoutId(tag);
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }

    @Override
    public String convertBrIdToString(int id) {
        for(DataBinderMapper mapper : mMappers) {
            String result = mapper.convertBrIdToString(id);
            if (result != null) {
                return result;
            }
        }
        return null;
    }
}
