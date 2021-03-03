package com.test.recyclerviewexperiment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.bumptech.glide.Glide
import com.test.recyclerviewexperiment.databinding.ActivityMainBinding
import com.test.recyclerviewexperiment.databinding.ItemMainBinding

class MainActivity : AppCompatActivity(), MyAdapter.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    private val myAdapter = MyAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (binding.rvMain.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        binding.rvMain.adapter = myAdapter

        myAdapter.submitList(DUMMY_DATA)
    }

    override fun onClick(myModel: MyModel) {
        val currList = myAdapter.currentList
        val mList =
            currList.map { if (it.id == myModel.id) it.copy(isExpanded = !it.isExpanded) else it.copy() }
        myAdapter.submitList(mList)
    }

    override fun goToDetail(url: String, imageView: ImageView) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, imageView, imageView.transitionName).toBundle()
        Intent(this, DetailActivity::class.java).putExtra(IMAGE_URL_KEY, url).let {
            startActivity(it, options)
        }
    }
}

class MyAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<MyModel, MyAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        val payload = payloads.firstOrNull()
        if (payload != null && payload == Status.CHANGE) holder.setExpanded(getItem(position).isExpanded)
        else super.onBindViewHolder(holder, position, payloads)
    }

    class ViewHolder private constructor(private val binding: ItemMainBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MyModel, onClickListener: OnClickListener) {
            setExpanded(item.isExpanded)
            Glide.with(binding.root).load("https://image.tmdb.org/t/p/w500" + item.image)
                .into(binding.ivMain)
            binding.ivMain.transitionName = "https://image.tmdb.org/t/p/w500" + item.image
            binding.tvMain.text = item.overview

            binding.root.setOnClickListener {
                onClickListener.onClick(item)
            }

            binding.tvMain.setOnClickListener {
                onClickListener.goToDetail(
                    "https://image.tmdb.org/t/p/w500" + item.image,
                    binding.ivMain
                )
            }
        }

        fun setExpanded(isExpanded: Boolean) {
            binding.tvMain.isVisible = isExpanded
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemMainBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<MyModel>() {
        override fun areItemsTheSame(oldItem: MyModel, newItem: MyModel) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: MyModel, newItem: MyModel) = oldItem == newItem

        override fun getChangePayload(oldItem: MyModel, newItem: MyModel): Any? {
            return if (oldItem.isExpanded != newItem.isExpanded) Status.CHANGE
            else null
        }
    }

    interface OnClickListener {
        fun onClick(myModel: MyModel)
        fun goToDetail(url: String, imageView: ImageView)
    }

    enum class Status {
        CHANGE
    }
}

data class MyModel(
    val id: Int,
    val image: String,
    val overview: String,
    val isExpanded: Boolean = false
)

val DUMMY_DATA = listOf(
    MyModel(
        1,
        "/d0d0gI46dadUPwF4t5XluXR96eA.jpg",
        "Wanda Maximoff and Vision—two super-powered beings living idealized suburban lives—begin to suspect that everything is not as it seems."
    ),
    MyModel(
        2,
        "/wRbjVBdDo5qHAEOVYoMWpM58FSA.jpg",
        "Set in the present, the series offers a bold, subversive take on Archie, Betty, Veronica and their friends, exploring the surreality of small-town life, the darkness and weirdness bubbling beneath Riverdale’s wholesome facade."
    ),
    MyModel(
        3,
        "/rqeYMLryjcawh2JeRpCVUDXYM5b.jpg",
        "Sheriff's deputy Rick Grimes awakens from a coma to find a post-apocalyptic world dominated by flesh-eating zombies. He sets out to find his family and encounters many other survivors along the way."
    ),
    MyModel(
        4,
        "/kl07N07l4XNjXF48oujzWXs40Dw.jpg",
        "In an inaccessible place between the mountains and isolated from the world, a school is located next to an old monastery. The students are rebellious and problematic young people who live under the strict and severe discipline imposed by the center to reintegrate them into society. The surrounding forest is home to ancient legends, threats that are still valid and that will immerse them in terrifying adventures."
    ),
    MyModel(
        5,
        "/4EYPN5mVIhKLfxGruy7Dy41dTVn.jpg",
        "Bored and unhappy as the Lord of Hell, Lucifer Morningstar abandoned his throne and retired to Los Angeles, where he has teamed up with LAPD detective Chloe Decker to take down criminals. But the longer he's away from the underworld, the greater the threat that the worst of humanity could escape."
    ),
    MyModel(
        6,
        "/6tfT03sGp9k4c0J3dypjrI8TSAI.jpg",
        "A young surgeon with Savant syndrome is recruited into the surgical unit of a prestigious hospital. The question will arise: can a person who doesn't have the ability to relate to people actually save their lives"
    ),
    MyModel(
        7,
        "/6SJppowm7cdQgLkvoTlnTUSbjr9.jpg",
        "After years of facing megalomaniacal supervillains, monsters wreaking havoc on Metropolis, and alien invaders intent on wiping out the human race, The Man of Steel aka Clark Kent and Lois Lane come face to face with one of their greatest challenges ever: dealing with all the stress, pressures and complexities that come with being working parents in today's society."
    ),
    MyModel(
        8,
        "/clnyhPqj1SNgpAdeSS6a6fwE6Bo.jpg",
        "Follows the personal and professional lives of a group of doctors at Seattle’s Grey Sloan Memorial Hospital."
    ),
    MyModel(
        9,
        "/qTZIgXrBKURBK1KrsT7fe3qwtl9.jp",
        "In a place where young witches, vampires, and werewolves are nurtured to be their best selves in spite of their worst impulses, Klaus Mikaelson’s daughter, 17-year-old Hope Mikaelson, Alaric Saltzman’s twins, Lizzie and Josie Saltzman, among others, come of age into heroes and villains at The Salvatore School for the Young and Gifted."
    ),
    MyModel(
        10,
        "/lJA2RCMfsWoskqlQhXPSLFQGXEJ.jpg",
        "After a particle accelerator causes a freak storm, CSI Investigator Barry Allen is struck by lightning and falls into a coma. Months later he awakens with the power of super speed, granting him the ability to move through Central City like an unseen guardian angel. Though initially excited by his newfound powers, Barry is shocked to discover he is not the only \\\"meta-human\\\" who was created in the wake of the accelerator explosion -- and not everyone is using their new powers for good. Barry partners with S.T.A.R. Labs and dedicates his life to protect the innocent. For now, only a few close friends and associates know that Barry is literally the fastest man alive, but it won't be long before the world learns what Barry Allen has become...The Flash."
    ),
)

val IMAGE_URL_KEY = "IMAGE_URL_KEY"