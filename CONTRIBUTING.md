
## Contributing
Find an issue.

### Basics
Let's not push stuff to master. You might have to do a gradle clean after checking out a new branch. Typically, waiting for the gradle sync to finish is enough for Android Studio to be happy, but if there are build errors, gradle clean should do the trick.
```
$ git checkout -b [my-feature-branch]
# Do your awesome feature changes here
# Test your awesome feature changes
$ git add [whatever files]
$ git commit -m "nice commit message"
# bonus points if you add git commit -m "Whatever message, fixes #[isssue number]
$ git push
```
Open a Pull Request, wait for whatever code review we may do, and get the thumbs up.

### Keep master and your branches updated
Once a pull request gets merged, you'll have to update your master branch.
```
$ git checkout master
$ git pull [--rebase origin master]
```
If you ever get something along the lines of `your branch and master have diverged with 2 commits and 10 commits respectively` after doing a `git status`, then rebase or merge from origin/master.

```
$ git fetch origin
$ git rebase origin/master
$ git push -f
```
or
```
$ git fetch origin
$ git merge origin/master
$ git push -f
```
