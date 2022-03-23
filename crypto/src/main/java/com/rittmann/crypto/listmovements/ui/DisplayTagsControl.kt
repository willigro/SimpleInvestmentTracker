package com.rittmann.crypto.listmovements.ui

import android.content.Context
import android.view.View
import co.lujun.androidtagview.TagContainerLayout
import co.lujun.androidtagview.TagView
import com.rittmann.common.extensions.ListableString
import com.rittmann.common.extensions.getColorByRes
import com.rittmann.common.extensions.toStringList
import com.rittmann.crypto.R
import java.io.Serializable

class DisplayTagsControl(
    private val context: Context,
    private val container: View, // view.findViewById(R.id.tagcontainerLayout)
    private var tags: ArrayList<Tag>,
    private val changeColorOnSelect: Boolean = true,
    controlOptions: Boolean = false,
    private val onClickTag: (tag: Tag?, selected: Boolean, execute: Boolean) -> Unit
) {

    private var tagContainerLayout: TagContainerLayout =
        container.findViewById(R.id.tag_container_layout)

    private var tagsStr: List<String> = arrayListOf()

    init {

        setTags(tags, onClickTag)

        if (controlOptions)
            configureOptions()
    }

    fun updateTags(tags: ArrayList<Tag>) {
        this.tags = tags
        setTags(tags, onClickTag)
    }

    private fun setTags(tags: List<Tag>, onClickTag: (Tag, Boolean, Boolean) -> Unit) {
        this.tagsStr = tags.toStringList()
        tagContainerLayout.backgroundColor =
            context.getColorByRes(R.color.robbie_color_light_background_primary)

        tagContainerLayout.tagBackgroundColor =
            context.getColorByRes(R.color.robbie_color_dark_background_secondary)

        tagContainerLayout.tagTextColor =
            context.getColorByRes(R.color.robbie_button_text_color_secondary)

        tagContainerLayout.tagBorderColor =
            context.getColorByRes(R.color.robbie_button_text_color_secondary)

        tagContainerLayout.isTagViewSelectable = true

        tagContainerLayout.tagTextSize =
            context.resources.getDimension(R.dimen.robbie_text_size_sb2)

        tagContainerLayout.setOnTagClickListener(object : TagView.OnTagClickListener {
            override fun onTagClick(position: Int, text: String?) {
                tagContainerLayout.getTagView(position).apply {

                    if (changeColorOnSelect) {
                        if (isViewSelected) {
                            unselect(this)
                        } else {
                            select(this)
                        }
                    }

                    onClickTag(tags[position], isViewSelected, true)
                }
            }

            override fun onTagLongClick(position: Int, text: String?) {
            }

            override fun onSelectedTagDrag(position: Int, text: String?) {
            }

            override fun onTagCrossClick(position: Int) {
            }
        })

        tagContainerLayout.tags = tagsStr

        tags.forEach {
            if (it.isDefault)
                highlightedTag(it)
        }
    }

    fun configureOptions() {
//        container.findViewById<View>(R.id.btn_select_all_tags)?.setOnClickListener {
//            selectAll()
//            onClickTag(null, false, true)
//        }
//
//        container.findViewById<View>(R.id.btn_unselect_all_tags)?.setOnClickListener {
//            unselectAll()
//            onClickTag(null, false, true)
//        }

        forceSelect()
    }

    fun forceSelect() {
        tags.forEachIndexed { index, tag ->
            if (tag.itemSelected) {
                select(tagContainerLayout.getTagView(index))
            }
        }
    }

    fun selectAll() {
        tags.forEachIndexed { index, tag ->
            select(tagContainerLayout.getTagView(index))
            onClickTag(tag, true, false)
        }
    }

    fun unselectAll() {
        tags.forEachIndexed { index, tag ->
            unselect(tagContainerLayout.getTagView(index))
            onClickTag(tag, false, false)
        }
    }

    fun addTag(item: Tag) {
        tags.add(item)
        tagsStr = tags.toStringList()
        tagContainerLayout.addTag(item.name)
    }

    fun removeTag(item: Tag) {
        for (i in tags.indices) {
            if (item.id == tags[i].id) {
                tagContainerLayout.removeTag(i)
                tags.removeAt(i)
                tagsStr = tags.toStringList()
                break
            }
        }
    }

    fun unselect(item: Tag) {
        for (i in tags.indices) {
            if (item.id == tags[i].id) {
                unselect(tagContainerLayout.getTagView(i))
                break
            }
        }
    }

    fun select(item: TagView) {
        item.apply {
            tagSelectedBackgroundColor =
                context.getColorByRes(R.color.robbie_color_dark_background_primary)
            setTagTextColor(context.getColorByRes(R.color.robbie_button_text_color_primary))
            selectView()
        }
    }

    fun select(item: Tag) {
        item.apply {
            for (i in tags.indices) {
                if (item.id == tags[i].id) {
                    select(tagContainerLayout.getTagView(i))
                    break
                }
            }
        }
    }

    fun unselect(item: TagView) {
        item.apply {
            tagSelectedBackgroundColor =
                context.getColorByRes(R.color.robbie_color_dark_background_secondary)
            setTagTextColor(context.getColorByRes(R.color.robbie_button_text_color_secondary))
            deselectView()
        }
    }

    fun highlightedTag(tag: Tag) {
        for (i in tags.indices) {
            if (tag.id == tags[i].id) {
                tagContainerLayout.getTagView(i).apply {
                    tagBackgroundColor =
                        context.getColorByRes(R.color.robbie_color_light_background_secondary)
                    setTagTextColor(context.getColorByRes(R.color.robbie_button_text_color_secondary))
                    setTagBorderColor(context.getColorByRes(R.color.robbie_button_text_color_secondary))
                }
                break
            }
        }

    }
}

class Tag(
    var id: Long? = null, // need be null for the room increment it

    var name: String = "",

    var isDefault: Boolean = false
) : Serializable, ListableString {

    @Transient
    var itemSelected: Boolean = false

    override fun label(): String = name
}