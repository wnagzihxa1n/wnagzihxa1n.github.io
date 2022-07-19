echo "This will purge all posts and pages!!!"
echo "If it's not what you want, Ctrl-C."
echo "Press enter to proceed..."
read

if [ -d _site ]; then
	rm -r ./_site
fi

if [ -e CNAME ]; then
	rm CNAME
fi

if [ -e _TODO.md ]; then
	rm _TODO.md
fi

rm -r ./_pages/*
rm -r ./_posts/*
rm -r ./_drafts/*
rm ./_includes/disqus.html && touch ./_includes/disqus.html
rm -r ./images/*

echo "Press enter to generate a demo post..."
echo "If it's not what you want, Ctrl-C."
read


cat _demo.md > ./_posts/2016-12-22-my-first-post.md 

cat > ./_pages/about.md << EOF
---
title: "About"
permalink: /about/
layout: page
---

你可以在此新建Page。事实上，所有不在_posts目录里的文章，都会被jekyll认为是Page。

EOF

echo "DONE."
echo "Now you can run 'jekyll serve'"
