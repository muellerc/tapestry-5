# Copyright 2012 The Apache Software Foundation
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http:#www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# ##core/palette
#
# Support for the `core/Palette` component.
define ["core/dom", "_"],
  (dom, _) ->
    class PaletteController

      constructor: (id) ->
        @selected = (dom id)
        container = @selected.findContainer ".t-palette"
        @available = container.findFirst ".t-palette-available select"
        @hidden = container.findFirst "input[type=hidden]"

        @select = container.findFirst "[data-action=select]"
        @deselect = container.findFirst "[data-action=deselect]"

        @moveUp = container.findFirst "[data-action=move-up]"
        @moveDown = container.findFirst "[data-action=move-down]"

        # Track where reorder is allowed based on whether the buttons actually exist
        @reorder = @moveUp isnt null

        @valueToOrderIndex = {}

        _.each @available.element.options, (option, i) =>
          @valueToOrderIndex[option.value] = i

        # This occurs even when the palette is disabled, to present the
        # values correctly. Otherwise it looks like nothing is selected.
        @initialTransfer()

        unless @selected.element.disabled
          @updateButtons()
          @bindEvents()

      initialTransfer: ->
        # Get the values for options that should move over
        values = JSON.parse @hidden.value()
        valueToPosition = {}

        _.each values, (v, i) -> valueToPosition[v] = i

        e = @available.element

        movers = []

        for i in [(e.options.length - 1)..0] by -1
          option = e.options[i]
          value = option.value
          pos = valueToPosition[value]
          unless pos is undefined
            movers[pos] = option
            e.remove i

        for option in movers
          @selected.element.add option

      updateHidden: ->
        values = _.pluck(this.selected, "value")
        hidden.value JSON.stringify values

      bindEvents: ->
        @select.on "click", =>
          @doSelect()
          return false

        @deselect.on "click", =>
          @doDeselect()
          return false

      updateButtons: ->
        @select.element.disabled = @available.element.selectedIndex < 0

        nothingSelected = @selected.element.selectedIndex < 0

        @deselect.element.disabled = nothingSelected

        if @reorder
          @moveUp.disabled = nothingSelected or @allSelectionsAtTop()
          @moveDown.disabled = nothingSelected or @allSelectionsAtBottom()

      transferOptions: (from, to, atEnd) ->
        if from.element.selectedIndex is -1
          return

        _(to.element.options).each (o) -> o.selected = false

        movers = @removeSelectedOptions from

        @moveOptions movers, to, atEnd

      removeSelectedOptions: (select) ->
        movers = []
        e = select.element
        options = e.options

        for i in [(e.length - 1)..(e.selectedIndex)] by -1
          o = options[i]
          if o.selected
            select.remove i
            movers.unshift o

        return movers

      moveOptions: (movers, to, atEnd) ->
        _.each movers, (o) =>
          @moveOption o, to, atEnd

        @updateHidden()
        @updateButtons()

      moveOptions: (option, to, atEnd) ->
        before = null

        unless atEnd
          optionOrder = @valueToOrderIndex[option.value]
          candidate = _.find to.element.options, (o) => @valueToOrderIndex[o.value] > optionOrder
          if candidate
            before = candidate

        @addOption to, option, before

      addOption: (to, option, before) ->
        try
          to.element.add option, before
        catch ex
          if before is null
            # IE throws an exception about type mismatch; here's the fix:
            to.add option
          else
            to.add option, before.index

      indexOfLastSelection: (select) ->
        e = select.element
        if e.selectedIndex < 0
          return -1

        for i in  [(e.options.length -1)..(e.selectedIndex + 1)] by -1
          if e.options[i].selected
            return i

        return -1

      allSelectionsAtTop: ->
        last = @indexOfLastSelection @selected
        options = @selected.options

        _(options[0..last]).all (o) -> o.selected

      allSelectionsAtBottom: ->
        last = @selected.element.selectedIndex

        _(options[last..]).all (o) -> o.selected


    initialize = (id) ->
      new PaletteController(id)

    # Export just the initialize function
    return initialize