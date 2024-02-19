import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("app:imageResource")
fun setImageResource(imageView: ImageView, resource: Int) {
    imageView.setImageResource(resource)
}